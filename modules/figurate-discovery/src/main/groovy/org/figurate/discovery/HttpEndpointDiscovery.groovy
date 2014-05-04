package org.figurate.discovery

import org.apache.felix.scr.annotations.*
import org.osgi.service.http.HttpService

import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

/**
 * Created by fortuna on 3/05/14.
 */
@Component(immediate = true)
@org.apache.felix.scr.annotations.Reference(referenceInterface = HttpService, bind = 'bind', unbind = 'unbind',
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
class HttpEndpointDiscovery {

    JmDNS jmDNS

    def serviceInfos = [:]

    @Activate
    void activate() {
        jmDNS = JmDNS.create()
        serviceInfos.values().each {
            jmDNS.registerService(it)
        }
    }

    @Deactivate
    void deactivate() {
        jmDNS.unregisterAllServices()
        jmDNS.close()
    }

    void bind(HttpService httpService, Map<String, ?> properties) {
        properties['osgi.http.service.endpoints'].each {
            String serviceType = (it =~ /^([^:]*).*$/)[0][1]
            String name = System.properties.getProperty('figurate.discovery.name', it)
            int port = (it =~ /^.*:(\d*)\//)[0][1] as int
            String description = System.properties.getProperty('figurate.discovery.description', 'Figurate HTTP Service')
            def serviceInfo = ServiceInfo.create("_${serviceType}._tcp.local.",
                    name, port, description);
            if (jmDNS) {
                jmDNS.registerService(serviceInfo)
            }
            serviceInfos[it] = serviceInfo
        }
    }

    void unbind(HttpService httpService, Map<String, ?> properties) {
        properties['osgi.http.service.endpoints'].each {
            def serviceInfo = serviceInfos.remove(it)
            if (serviceInfo) {
                jmDNS.unregisterService(serviceInfo)
            }
        }
    }
}
