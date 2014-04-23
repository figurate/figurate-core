package org.figurate.osgi.http

import org.figurate.osgi.ServiceName
import org.figurate.test.AbstractFrameworkSpecification

/**
 * Created by fortuna on 23/04/14.
 */
class ServiceInfoSpec extends AbstractFrameworkSpecification {

    @Override
    def getConfigLocation() {
        return new File('modules/figurate-osgi/src/test/resources/config/HttpVersionSpec.config')
    }

    def 'verify service is loaded'() {
        setup:
        def endpointService = binding.serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${ServiceInfo.canonicalName})"
            }
        })

        expect:
        endpointService != null
    }

    def 'test service info rest endpoint'() {
        expect:
        new URL('http://localhost:8081/service/all?objectClass=javax.servlet.Servlet').text == '{}'
    }
}
