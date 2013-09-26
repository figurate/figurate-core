package org.figurate.osgi

import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference;

import spock.lang.Specification

class ServiceProxySpec extends Specification {

	def 'verify exception is thrown when service is not available'() {
		setup: 'mock bundle context'
		BundleContext bundleContext = Mock()
		
		and: 'create service proxy'
		List proxy = ServiceProxy.newProxyInstance(getClass().classLoader, List, bundleContext)
		
		when:
		proxy.size()
		
		then:
		thrown(ServiceNotAvailableException)
	}
	
	def 'verify service is called when service is available'() {
		setup: 'mock bundle context'
		BundleContext bundleContext = Mock()
		def reference = {'reference'} as ServiceReference<List>
		bundleContext.getServiceReferences(List.name, null) >> reference
		bundleContext.getService(reference) >> ['1']
		
		and: 'create service proxy'
		List proxy = ServiceProxy.newProxyInstance(getClass().classLoader, List, bundleContext)
		
		expect:
		proxy.size() == 1
		proxy[0] == '1'
	}
}
