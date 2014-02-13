package org.figurate.osgi

import groovy.transform.CompileStatic

import org.osgi.framework.BundleContext
import org.osgi.framework.Filter
import org.osgi.framework.FrameworkUtil
import org.osgi.framework.InvalidSyntaxException
import org.osgi.framework.ServiceReference
import org.osgi.framework.ServiceRegistration
import org.osgi.util.tracker.ServiceTracker

@CompileStatic
class OsgiServiceLocator implements ServiceLocator {

	private final BundleContext context
	
	private final Map<ServiceName, ServiceTracker> serviceTrackers

	OsgiServiceLocator(BundleContext context) {
		this.context = context
		serviceTrackers = new HashMap<ServiceName, ServiceTracker>()
	}
	
	@Override
	<T> T findService(ServiceName name) throws ServiceNotAvailableException {
        ServiceTracker tracker = serviceTrackers.get(name)
        if (tracker == null) {
            synchronized (serviceTrackers) {
                tracker = serviceTrackers.get(name)
                if (tracker == null) {
                    try {
                        final Filter filter = context.createFilter(name.getFilter())
                        tracker = new ServiceTracker(context, filter, null)
                        tracker.open()
                        serviceTrackers.put(name, tracker)
                    } catch (InvalidSyntaxException e) {
                        throw new IllegalArgumentException(e)
                    }
                }
            }
        }
        final T service = (T) tracker.getService()
        if (service == null) {
            throw new ServiceNotAvailableException("Service matching [" + name.getFilter() + "] not found.")
        }
        return service
    }

	void reset() {
		for (ServiceTracker tracker : serviceTrackers.values()) {
			tracker.close()
		}
		serviceTrackers.clear()
	}

    static <T> T findService(Class<T> serviceClass) {
        T service = null
        BundleContext context = FrameworkUtil.getBundle(serviceClass).bundleContext
        ServiceReference<T> reference = context.getServiceReference(serviceClass)
        if (reference) {
            context.getService(reference)
        }
        service
    }
}
