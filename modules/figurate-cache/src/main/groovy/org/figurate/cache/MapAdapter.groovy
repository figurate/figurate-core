package org.figurate.cache

import groovy.transform.CompileStatic
import org.gcontracts.annotations.Requires

@CompileStatic
class MapAdapter implements CacheAdapter {

    private final Map<?, ?> cache

    @Requires({cache != null})
    MapAdapter(Map<?, ?> cache) {
        this.cache = cache
    }
    
	@Override
    public <T> T get(CacheEntry entry, Object...args) {
        final String key = entry.getKey(args)
        T value = (T) cache.get(key)
        if (value == null) {
            value = (T) entry.load(args)
            cache[key] = value
        }
        return value
    }
}
