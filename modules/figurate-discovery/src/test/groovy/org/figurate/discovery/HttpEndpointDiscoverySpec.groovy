package org.figurate.discovery

import org.figurate.test.AbstractFrameworkSpecification

import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

/**
 * Created by fortuna on 3/05/14.
 */
class HttpEndpointDiscoverySpec extends AbstractFrameworkSpecification {

    @Override
    def getConfigLocation() {
        return new File('tests/src/test/resources/config/DefaultEndpoint.config')
    }

    def 'test list http services'() {
        setup:
        JmDNS jmDNS = JmDNS.create()
        ServiceInfo serviceInfo = jmDNS.list("_http._tcp.local.").find {it.name = 'figurate.test'}

        expect:
        new URL("http://${serviceInfo.hostAddress}:${serviceInfo.port}/discovery/list?type=http").text == '{}'
    }

    def 'test list https services'() {
        setup:
        JmDNS jmDNS = JmDNS.create()
        ServiceInfo serviceInfo = jmDNS.list("_https._tcp.local.").find {it.name = 'figurate.test'}

        expect:
        new URL("https://${serviceInfo.hostAddress}:${serviceInfo.port}/discovery/list?type=https").text == '{}'
    }
}
