package org.figurate.test

import spock.lang.Shared

import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

/**
 * Created by fortuna on 1/05/14.
 */
abstract class AbstractEndpointSpecification extends AbstractFrameworkSpecification {

    @Shared
    ServiceInfo serviceInfo

    def setupSpec() {
        JmDNS jmDNS = JmDNS.create()
        serviceInfo = jmDNS.list("_http._tcp.local.").find {it.name = 'figurate.test'}
    }

    @Override
    def getConfigLocation() {
        return new File('tests/src/test/resources/config/DefaultEndpoint.config')
    }

    URL getEndpointUrl(String endpointPath) {
        new URL("http://${serviceInfo.hostAddress}:${serviceInfo.port}$endpointPath")
    }
}
