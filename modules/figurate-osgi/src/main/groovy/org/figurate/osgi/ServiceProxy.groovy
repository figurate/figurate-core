package org.figurate.osgi

import groovy.transform.CompileStatic

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

import org.osgi.framework.BundleContext
import org.osgi.util.tracker.ServiceTracker

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
		return Proxy.newProxyInstance(classLoader, [serviceClass] as Class[],
				new ServiceInvocationHandler(serviceClass, context))
	}
	
	private static class ServiceInvocationHandler implements InvocationHandler {
		
		private final ServiceTracker tracker
		
		public ServiceInvocationHandler(Class<?> serviceClass, BundleContext context) {
			tracker = new ServiceTracker(context, serviceClass.getName(), null)
			tracker.open()
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final Object service = tracker.getService()
			if (service == null) {
				throw new ServiceNotAvailableException(tracker.getTracked() as String)
			}
			return method.invoke(service, args)
		}
	}
}
