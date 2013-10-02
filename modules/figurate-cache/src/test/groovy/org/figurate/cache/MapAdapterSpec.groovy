package org.figurate.cache

import spock.lang.Specification

import org.infinispan.manager.DefaultCacheManager
import org.infinispan.Cache

class MapAdapterSpec extends Specification {

	def 'verify cache get'() {
		setup:
		MapAdapter cache = [[:]]
		
		when:
		CacheEntryImpl entry = []
		
		then: 'subsequent retrievals should return the cached value'
		def value = cache.get(entry, 2)
		assert cache.get(entry, 2) == value
	}

	def 'verify infinispan cache get'() {
		setup:
		DefaultCacheManager cacheManager = []
		Cache cache = cacheManager.cache
		
		when:
		CacheEntryImpl entry = []
		
		then: 'subsequent retrievals should return the cached value'
		def value = cache.get(entry, 2)
		assert cache.get(entry, 2) == value
	}
	
	
	class CacheEntryImpl implements CacheEntry {
		@Override
		public String getKey(Object... args) {
			return "key.${args[0]}"
		}
		
		@Override
		public Object load(Object... args) {
			return new Random(args[0]).nextInt()
		}
	}
}
