package org.figurate;

public interface RetryHandler {

    /**
     * Handle exception thrown on method invocation.
     * @param e the exception
     * @return true if the method invocation should be retried, otherwise false
     */
    boolean retryOnException(Throwable e);
}
