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

/**
 * Created by fortuna on 23/04/14.
 */
//@CompileStatic
@Component(immediate = true)
@Service(value = BundleInfo)
@Path("bundle")
@Description("Provides information about active OSGi bundles")
class BundleInfo {
    BundleContext context = FrameworkUtil.getBundle(BundleInfo).bundleContext

    @GET
    @Path('all')
    @Produces("application/json")
    String getSummaryAll() {
        def bundles = context.bundles.collect {[id: it.bundleId, name: it.symbolicName, state: it.state]}
        def builder = new JsonBuilder()
        builder(bundles: bundles)
        builder.toString()
    }
}
