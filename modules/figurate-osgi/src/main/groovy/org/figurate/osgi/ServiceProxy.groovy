package org.figurate.osgi

import groovy.transform.CompileStatic

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

import org.osgi.framework.BundleContext
import org.osgi.util.tracker.ServiceTracker

/**
 * Provides a mechanism for referencing an OSGi service that has not yet been loaded. This might be useful in cases where
 * you know (!) the service will be available at the time of method invocation, and is really just a convenience
 * mechanism for wrapping {@link ServiceTracker} functionality.
 *
 * Note that there are very specific requirements for when this proxy may be used:
 * <ul>
 *     <li>The service must be available at method invocation or a {@link ServiceNotAvailableException} will be thrown</li>
 *     <li>
 *         The service class must be loaded by the specified classloader or method invocation will cause an {@link IllegalArgumentException}
 *         to be thrown. This means that if a service class resides within the bundle from where the service originates
 *         you will need to delegate classloading to that bundle (see {@link org.osgi.framework.Bundle#loadClass(java.lang.String)})
 *     </li>
 */
@CompileStatic
class ServiceProxy {
	
	/**
	 * @param <T> the service type
	 * @param classLoader the classloader for the proxy
	 * @param serviceClass the service class to proxy
	 * @param context the bundle context use to locate the underlying service
	 * @return a new proxy for the specified service type
	 */
	static <T> T newProxyInstance(ClassLoader classLoader, Class<T> serviceClass, BundleContext context) {
		return (T) Proxy.newProxyInstance(classLoader, [serviceClass] as Class[],
				new ServiceInvocationHandler(serviceClass, context))
	}

	/**
	 * @param <T> the service type
	 * @param classLoader the classloader for the proxy
	 * @param serviceClass the service class to proxy
	 * @param context the bundle context use to locate the underlying service
     * @param timeout the time (in milliseconds) to wait for a service to become available on each proxy method call
	 * @return a new proxy for the specified service type
	 */
	static <T> T newProxyInstance(ClassLoader classLoader, Class<T> serviceClass, BundleContext context, long timeout) {
		return (T) Proxy.newProxyInstance(classLoader, [serviceClass] as Class[],
				new ServiceInvocationHandler(serviceClass, context, timeout))
	}

	private static class ServiceInvocationHandler implements InvocationHandler {
		
		private final ServiceTracker tracker

        private final String toStringValue

        private final long waitForServiceTimeout
		
  		public ServiceInvocationHandler(Class<?> serviceClass, BundleContext context) {
            this(serviceClass, context, 0)
        }

		public ServiceInvocationHandler(Class<?> serviceClass, BundleContext context, long timeout) {
			tracker = new ServiceTracker(context, serviceClass.name, null)
			tracker.open()

            this.waitForServiceTimeout = timeout
            toStringValue = "ServiceProxy(objectClass=$serviceClass)"
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.name == 'toString') {
                return toStringValue
            }

            if (waitForServiceTimeout > 0) {
                tracker.waitForService(waitForServiceTimeout)
            }
			final Object service = tracker.getService()
			if (service == null) {
				throw new ServiceNotAvailableException(tracker.tracked as String)
			}
			return method.invoke(service, args)
		}
	}
}
