package org.figurate.test

import org.osgi.framework.FrameworkEvent
import org.osgi.framework.FrameworkListener
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
        binding.bundleContext.addFrameworkListener({ e ->
            if (e.type == FrameworkEvent.STARTED) {
                JmDNS jmDNS = JmDNS.create()
                serviceInfo = jmDNS.list("_https._tcp.local.").find {it.name == 'figurate.test'}
            }
        } as FrameworkListener)
    }

    @Override
    def getConfigLocation() {
//        return new File('tests/src/test/resources/config/DefaultEndpoint.config')
        return new File("build/test-config/${new File(System.properties['user.dir']).name}.config")
    }

    URL getEndpointUrl(String endpointPath) {
//        new URL("https://${serviceInfo.hostAddress}:${serviceInfo.port}$endpointPath")
        new URL("https://localhost:8443$endpointPath")
    }
}
