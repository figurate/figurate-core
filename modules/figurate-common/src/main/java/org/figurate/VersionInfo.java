package org.figurate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("version")
public interface VersionInfo {

  @GET
  @Path("versionString")
  String getVersionString();
}
