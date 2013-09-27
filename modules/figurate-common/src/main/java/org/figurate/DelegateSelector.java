package org.figurate;

public interface DelegateSelector<T> {

    /**
     * @return a reference to the selected delegate
     */
    T getDelegate();
}
