package org.figurate.cache

import groovy.transform.CompileStatic

import net.sf.ehcache.Cache
import net.sf.ehcache.Element

@CompileStatic
class EhCacheAdapter implements CacheAdapter {

    private final Cache cache
    
    EhCacheAdapter(Cache cache) {
        this.cache = cache
    }
    
	@Override
    public <T> T get(CacheEntry entry, Object...args) {
        T value = null
        final String key = entry.getKey(args)
        Element cacheElement = cache.get(key)
        if (cacheElement != null) {
            value = cacheElement.objectValue
        }
        else {
            value = entry.load(args)
            cache.put(new Element(key, value))
        }
        return value
    }
}
