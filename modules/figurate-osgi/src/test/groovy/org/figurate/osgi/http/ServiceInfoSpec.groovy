package org.figurate.osgi.http

import org.figurate.ServiceName
import org.figurate.test.AbstractEndpointSpecification
import org.osgi.service.http.HttpService

/**
 * Created by fortuna on 23/04/14.
 */
class ServiceInfoSpec extends AbstractEndpointSpecification {

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
//        new URL('http://localhost:8081/service/all?objectClass=javax.servlet.Servlet').text == '{}'
        getEndpointUrl("/service/all?objectClass=${HttpService.name}").text == '{}'
    }
}
