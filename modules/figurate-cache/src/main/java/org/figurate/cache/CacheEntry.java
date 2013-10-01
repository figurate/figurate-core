package org.figurate.cache;

public interface CacheEntry {

    String getKey(Object...args);
    
    Object load(Object...args);
}
