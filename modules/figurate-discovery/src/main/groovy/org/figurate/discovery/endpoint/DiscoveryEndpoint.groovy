package org.figurate.discovery.endpoint

import groovy.json.JsonBuilder
import org.amdatu.web.rest.doc.Description
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Deactivate
import org.apache.felix.scr.annotations.Service

import javax.jmdns.JmDNS
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam

/**
 * Created by fortuna on 3/05/14.
 */
@Component(immediate = true)
@Service(value = DiscoveryEndpoint)
@Path("discovery")
@Description("List discoverable services")
class DiscoveryEndpoint {

    JmDNS jmDNS

    @Activate
    void activate() {
        jmDNS = JmDNS.create()
    }

    @Deactivate
    void deactivate() {
        jmDNS.close()
    }

    @GET
    @Path('list')
    @Produces("application/json")
    String listServices(@QueryParam('type') String serviceType) {
        def builder = new JsonBuilder()
        builder("$serviceType":
                jmDNS.list("_${serviceType}._tcp.local.").collectEntries {
                    ["$it.name": [
                            'server': it.server,
                            'ipAddress': it.hostAddress,
                            'port': it.port,
                            'description': it.niceTextString
                    ]]
                }
        )
        builder.toPrettyString()
    }
}
