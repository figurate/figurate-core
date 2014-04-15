package org.figurate.cache

import groovy.transform.CompileStatic

import net.sf.ehcache.Cache
import net.sf.ehcache.Element
import org.gcontracts.annotations.Requires

@CompileStatic
class EhCacheAdapter implements CacheAdapter {

    private final Cache cache

    @Requires({cache != null})
    EhCacheAdapter(Cache cache) {
        this.cache = cache
    }
    
	@Override
    public <T> T get(CacheEntry entry, Object...args) {
        T value = null
        final String key = entry.getKey(args)
        Element cacheElement = cache.get(key)
        if (cacheElement != null) {
            value = (T) cacheElement.objectValue
        }
        else {
            value = (T) entry.load(args)
            cache.put(new Element(key, value))
        }
        return value
    }
}
