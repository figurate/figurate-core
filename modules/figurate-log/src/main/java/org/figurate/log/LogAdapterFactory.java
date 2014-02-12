package org.figurate.log;

/**
 * Created by fortuna on 4/02/14.
 */
public interface LogAdapterFactory<T> {

    LogAdapter newInstance(T delegate);
}
