package org.figurate

import groovy.transform.CompileStatic

import java.lang.management.ManagementFactory
import java.util.concurrent.atomic.AtomicInteger

@CompileStatic
class IdGenerator {

    private final String prefix
    
    private final AtomicInteger count
    
    private final String pid
    
    /**
     * @param prefix an optional prefix for generated identifiers
     */
    IdGenerator(String prefix) {
        this.prefix = prefix
        this.count = []
        this.pid = ManagementFactory.runtimeMXBean.name
    }
    
    /**
     * @return an identifier guaranteed to be unique across processes
     */
    String generateId() {
        final StringBuilder b = []
        if (prefix) {
            b.append(prefix).append('-')
        }
        b.append(count.incrementAndGet()).append(':').append(pid)
        return b as String
    }
}
