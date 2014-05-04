package org.figurate.osgi.http

import org.figurate.ServiceName
import org.figurate.test.AbstractEndpointSpecification

/**
 * Created by fortuna on 23/04/14.
 */
class BundleInfoSpec extends AbstractEndpointSpecification {

    def 'verify service is loaded'() {
        setup:
        def endpointService = binding.serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${BundleInfo.canonicalName})"
            }
        })

        expect:
        endpointService != null
    }

    def 'test bundle info rest endpoint'() {
        expect:
        getEndpointUrl("/bundle/all").text == '{"bundles":[{"id":0,"name":"org.apache.felix.framework","state":32},{"id":1,"name":"slf4j.api","state":32},{"id":2,"name":"ch.qos.logback.core","state":32},{"id":3,"name":"ch.qos.logback.classic","state":32},{"id":4,"name":"osgi.cmpn","state":32},{"id":5,"name":"org.apache.felix.scr","state":32},{"id":6,"name":"org.apache.felix.dependencymanager","state":32},{"id":7,"name":"org.apache.felix.log","state":32},{"id":8,"name":"groovy-all","state":32},{"id":9,"name":"gpars.org","state":32},{"id":10,"name":"org.fusesource.jansi","state":32},{"id":11,"name":"org.apache.felix.http.bundle","state":32},{"id":12,"name":"org.amdatu.web.rest.jaxrs","state":32},{"id":13,"name":"org.amdatu.web.rest.wink","state":32},{"id":14,"name":"org.amdatu.web.rest.doc.swagger.ui","state":32},{"id":15,"name":"org.amdatu.web.rest.doc.swagger","state":32},{"id":16,"name":"org.amdatu.web.rest.doc","state":32},{"id":17,"name":"org.amdatu.web.resourcehandler","state":32},{"id":18,"name":"jackson-core-asl","state":32},{"id":19,"name":"jackson-jaxrs","state":32},{"id":20,"name":"jackson-mapper-asl","state":32},{"id":21,"name":"figurate-osgi","state":32},{"id":22,"name":"figurate-common","state":32},{"id":23,"name":"figurate-log","state":32}]}'
    }
}
