package org.figurate

import groovy.transform.CompileStatic

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

@CompileStatic
class DelegateInvocationHandler implements InvocationHandler {

    private final DelegateSelector<?> selector
    
    public DelegateInvocationHandler(DelegateSelector<?> selector) {
        this.selector = selector
    }
    
    /**
     * {@inheritDoc}
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(selector.delegate, args)
    }

}
