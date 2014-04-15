package org.figurate

import groovy.transform.CompileStatic
import org.gcontracts.annotations.Requires

import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

@CompileStatic
class RetryInvocationHandler implements InvocationHandler {

    private final Object target
    
    private final RetryHandler retryHandler
    
    /**
     * @param target the invocation target
     * @param retryHandler controls retry on invocation exceptions
     */
    @Requires({target != null && retryHandler != null})
    public RetryInvocationHandler(Object target, RetryHandler retryHandler) {
        this.target = target
        this.retryHandler = retryHandler
    }
    
    @Override
    @Requires({method != null})
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null
        boolean retry = true
        while (retry) {
            try {
                result = method.invoke(target, args)
                break
            }
            catch (InvocationTargetException e) {
                retry = retryHandler.retryOnException(e.targetException)
                if (!retry) {
                    throw e.targetException
                }
            }
        }
        return result
    }
}
