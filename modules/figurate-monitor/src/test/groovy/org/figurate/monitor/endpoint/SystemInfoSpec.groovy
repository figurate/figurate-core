package org.figurate.monitor.endpoint

import org.figurate.ServiceName
import org.figurate.test.AbstractEndpointSpecification

/**
 * Created by fortuna on 23/04/14.
 */
class SystemInfoSpec extends AbstractEndpointSpecification {

    def 'verify service is loaded'() {
        setup:
        def endpointService = binding.serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${SystemInfo.canonicalName})"
            }
        })

        expect:
        endpointService != null
    }

    def 'test memory info rest endpoint'() {
        expect:
        getEndpointUrl("/system/memory").text == '{}'
    }

    def 'test cpu info rest endpoint'() {
        expect:
        getEndpointUrl("/system/cpu").text == '{}'
    }

    def 'test filesystem info rest endpoint'() {
        expect:
        getEndpointUrl("/system/filesystem").text == '{}'
    }
}
