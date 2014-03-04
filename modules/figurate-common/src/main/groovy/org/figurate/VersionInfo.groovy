package org.figurate

import groovy.transform.CompileStatic

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Created by fortuna on 5/03/14.
 */
@Path("version")
@CompileStatic
class VersionInfo {

    String version

    @GET
    String getVersion() {
        version
    }
}
