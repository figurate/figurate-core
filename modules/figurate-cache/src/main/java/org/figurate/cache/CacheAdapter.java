package org.figurate.cache;

public interface CacheAdapter {

    <T> T get(CacheEntry entry, Object...args);
}
