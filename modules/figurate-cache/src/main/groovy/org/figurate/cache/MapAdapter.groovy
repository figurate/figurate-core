package org.figurate.cache

import groovy.transform.CompileStatic

@CompileStatic
class MapAdapter implements CacheAdapter {

    private final Map<?, ?> cache
    
    MapAdapter(Map<?, ?> cache) {
        this.cache = cache
    }
    
    <T> T get(CacheEntry entry, Object...args) {
        final String key = entry.getKey(args)
        T value = cache.get(key)
        if (value == null) {
            value = entry.load(args)
            cache.put(key, value)
        }
        return value
    }
}
