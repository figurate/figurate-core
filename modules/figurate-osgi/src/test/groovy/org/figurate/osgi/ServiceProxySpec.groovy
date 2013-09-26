package org.figurate.osgi

import org.osgi.framework.BundleContext

import spock.lang.Specification

class ServiceProxySpec extends Specification {

	def 'verify exception is thrown when service is not available'() {
		setup: 'mock bundle context'
		BundleContext bundleContext = Mock()
		
		and: 'create service proxy'
		List proxy = ServiceProxy.newProxyInstance(getClass().getClassLoader(), List, bundleContext)
		
		when:
		proxy.size()
		
		then:
		thrown(ServiceNotAvailableException)
	}
}
