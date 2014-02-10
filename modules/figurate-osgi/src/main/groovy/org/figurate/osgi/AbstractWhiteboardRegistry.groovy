package org.figurate.osgi

import groovy.transform.CompileStatic
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Deactivate

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Base class for registry implementations to assist implementation of the
 * whiteboard pattern.
 * 
 * @param <P>
 *            the publisher type
 * @param <S>
 *            the subscriber type
 */
@CompileStatic
@Component(componentAbstract = true)
abstract class AbstractWhiteboardRegistry<P, S> {

    private final List<P> publishers

    private final Map<S, Map<String, ?>> subscribers

    AbstractWhiteboardRegistry() {
        publishers = new CopyOnWriteArrayList<P>()
        subscribers = new ConcurrentHashMap<S, Map<String, ?>>()
    }

    /**
     * @param publisher the source
     * @param properties additional publishing properties
     */
    final void registerPublisher(P publisher, Map<String, ?> properties) {
        publishers.add(publisher)
        for (S subscriber : subscribers.keySet()) {
            subscribe(publisher, subscriber, subscribers.get(subscriber))
        }
    }

    /**
     * @param publisher the source
     * @param properties additional publishing properties
     */
    final void unregisterPublisher(P publisher, Map<String, ?> properties) {
        publishers.remove(publisher)
        for (S subscriber : subscribers.keySet()) {
            unsubscribe(publisher, subscriber, subscribers.get(subscriber))
        }
    }

    /**
     * @param subscriber the destination
     * @param properties additional subscription properties
     */
    final void registerSubscriber(S subscriber, Map<String, ?> properties) {
        subscribers.put(subscriber, properties);
        for (P publisher : publishers) {
            subscribe(publisher, subscriber, properties);
        }
    }

    /**
     * @param subscriber the destination
     * @param properties additional subscription properties
     */
    final void unregisterSubscriber(S subscriber,
            Map<String, ?> properties) {
        subscribers.remove(subscriber)
        for (P publisher : publishers) {
            unsubscribe(publisher, subscriber, properties)
        }
    }

    /**
     * Unregister all subscribers.
     */
    @Deactivate
    final void unregisterAll() {
        for (S subscriber : subscribers.keySet()) {
            unregisterSubscriber(subscriber, subscribers.get(subscriber))
        }
    }
    
    /**
     * @param publisher the source
     * @param subscriber the destination
     * @param properties additional subscription properties
     */
    protected abstract void subscribe(P publisher, S subscriber,
            Map<String, ?> properties)

    /**
     * @param publisher the source
     * @param subscriber the destination
     * @param properties additional subscription properties
     */
    protected abstract void unsubscribe(P publisher, S subscriber,
            Map<String, ?> properties)
}
