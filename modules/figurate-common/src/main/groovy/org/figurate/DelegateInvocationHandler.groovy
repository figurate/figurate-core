package org.figurate

import groovy.transform.CompileStatic
import org.gcontracts.annotations.Requires

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

@CompileStatic
class DelegateInvocationHandler implements InvocationHandler {

    private final DelegateSelector<?> selector

    @Requires({selector != null})
    public DelegateInvocationHandler(DelegateSelector<?> selector) {
        this.selector = selector
    }
    
    /**
     * {@inheritDoc}
     */
    @Requires({method != null && selector.delegate != null})
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(selector.delegate, args)
    }

}
