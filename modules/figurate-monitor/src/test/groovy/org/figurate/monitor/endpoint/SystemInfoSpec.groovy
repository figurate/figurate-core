package org.figurate.monitor.endpoint

import org.figurate.osgi.ServiceName
import org.figurate.test.AbstractFrameworkSpecification

/**
 * Created by fortuna on 23/04/14.
 */
class SystemInfoSpec extends AbstractFrameworkSpecification {

    @Override
    def getConfigLocation() {
        return new File('tests/src/test/resources/config/DefaultOSGi.config')
    }

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
        new URL('http://localhost:8081/system/memory').text == '{}'
    }

    def 'test cpu info rest endpoint'() {
        expect:
        new URL('http://localhost:8081/system/cpu').text == '{}'
    }

    def 'test filesystem info rest endpoint'() {
        expect:
        new URL('http://localhost:8081/system/filesystem').text == '{}'
    }
}
