package org.figurate.cache

import groovy.transform.CompileStatic
import org.gcontracts.annotations.Requires

@CompileStatic
class MapAdapter implements CacheAdapter {

    private final Map<String, Object> cache

    @Requires({cache != null})
    MapAdapter(Map<String, Object> cache) {
        this.cache = cache
    }
    
	@Override
    public <T> T get(CacheEntry entry, Object...args) {
        final String key = entry.getKey(args)
        T value = (T) cache.get(key)
        if (value == null) {
            value = (T) entry.load(args)
            cache.put(key, (Object) value)
        }
        return value
    }
}
