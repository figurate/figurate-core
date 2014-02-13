package org.figurate.log.osgi

import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.ReferenceCardinality
import org.apache.felix.scr.annotations.ReferencePolicy
import org.figurate.log.LogAdapter
import org.figurate.log.LogAdapterFactory
import org.figurate.osgi.AbstractWhiteboardRegistry
import org.osgi.framework.BundleContext
import org.osgi.framework.FrameworkUtil
import org.osgi.framework.ServiceRegistration
import org.osgi.service.log.LogService

/**
 * Created by fortuna on 4/02/14.
 */
@Component(immediate = true)
@org.apache.felix.scr.annotations.Reference(referenceInterface = LogService, bind = 'registerLogService', unbind = 'unregisterLogService', cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
@org.apache.felix.scr.annotations.Reference(referenceInterface = LogAdapterFactory, bind = 'registerLogAdapterFactory', unbind = 'unregisterLogAdapterFactory', cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
class LogAdapterRegistry extends AbstractWhiteboardRegistry<LogService, LogAdapterFactory<LogService>> {

    private BundleContext bundleContext = FrameworkUtil.getBundle(LogAdapterRegistry.class).bundleContext

    def registrations = [:]

    public void registerLogService(LogService logService, Map<String, ?> properties) {
        registerPublisher(logService, properties)
    }

    public void unregisterLogService(LogService logService, Map<String, ?> properties) {
        unregisterPublisher(logService, properties)
    }

    public void registerLogAdapterFactory(LogAdapterFactory<LogService> factory, Map<String, ?> properties) {
        registerSubscriber(factory, properties)
    }

    public void unregisterLogAdapterFactory(LogAdapterFactory<LogService> factory, Map<String, ?> properties) {
        unregisterSubscriber(factory, properties)
    }

    @Override
    protected void subscribe(LogService publisher, LogAdapterFactory<LogService> subscriber, Map<String, ?> properties) {
        LogAdapter logAdapter = subscriber.newInstance(publisher)
        def sr = bundleContext.registerService(LogAdapter, logAdapter, null)
        registrations[publisher] = sr
    }

    @Override
    protected void unsubscribe(LogService publisher, LogAdapterFactory<LogService> subscriber, Map<String, ?> properties) {
        ServiceRegistration registration = registrations.remove(publisher)
        registration.unregister()
    }
}
