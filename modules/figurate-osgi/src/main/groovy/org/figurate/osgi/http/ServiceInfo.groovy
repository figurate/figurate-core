package org.figurate.osgi.http

import groovy.json.JsonBuilder
import org.amdatu.web.rest.doc.Description
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.osgi.framework.BundleContext
import org.osgi.framework.FrameworkUtil

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam

/**
 * Created by fortuna on 7/03/14.
 */
//@CompileStatic
@Component(immediate = true)
@Service(value = ServiceInfo)
@Path("service")
@Description("Provides information about active OSGi services")
class ServiceInfo {

    BundleContext context = FrameworkUtil.getBundle(BundleInfo).bundleContext

    @GET
    @Path('all')
    @Produces("application/json")
    String getSummaryAll(@QueryParam("objectClass") String objectClass) {
        def services = context.bundles.collect {
            it.registeredServices.findAll {
                !objectClass || it.getProperty('objectClass').contains(objectClass)
            }.collect { service ->
            [
                    properties: service.propertyKeys.collectEntries {["$it": service.getProperty(it)]},
                    bundle: service.bundle.symbolicName,
                    usingBundles: service.usingBundles.collect {it.symbolicName}
            ]}}.flatten()
        def builder = new JsonBuilder()
        builder(services: services)
        builder.toPrettyString()
    }

    @GET
//    @Consumes("application/json")
    @Produces("text/html")
    @Description("Renders service information in HTML")
    String getDetail(@QueryParam("query") String query) {
//        def json = new JsonSlurper().parseText(query)
        getClass().getResource("/pages/service.html").text
//        "hellooooo $query"
    }
}
