package org.figurate.discovery.endpoint

import org.figurate.ServiceName
import org.figurate.test.AbstractEndpointSpecification
import spock.lang.Unroll

/**
 * Created by fortuna on 3/05/14.
 */
class DiscoveryEndpointSpec extends AbstractEndpointSpecification {

    @Override
    def getConfigLocation() {
        return new File('tests/src/test/resources/config/DefaultEndpoint.config')
    }

    def 'verify service is loaded'() {
        setup:
        def endpointService = binding.serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${DiscoveryEndpoint.canonicalName})"
            }
        })

        expect:
        endpointService != null
    }

    @Unroll
    def 'test list services endpoint'() {
        expect:
        getEndpointUrl("/discovery/list?type=$serviceType").text == '{}'

        where:
        serviceType << ['http', 'presence', 'workstation', 'ssh']
    }
}
