package org.figurate.test

import org.figurate.ScriptEvaluator

import org.figurate.osgi.FrameworkBuilder
import org.figurate.osgi.OsgiServiceLocator
import org.figurate.osgi.ServiceName
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceRegistration
import org.osgi.framework.launch.Framework
import spock.lang.Specification

import java.util.concurrent.TimeUnit

/**
 * Created by fortuna on 13/02/14.
 */
class HttpVersionSpec extends Specification {

    OsgiServiceLocator serviceLocator
    def evaluator
    def bundleContext

    def 'test version REST method'() {
        setup: 'Initialise framework'
        Framework osgi = new FrameworkBuilder().osgi {
            config {
                ['org.osgi.framework.storage': 'build/felix-cache',
                    'org.osgi.framework.storage.clean': 'onFirstInit',
                        'felix.log.level': '4',
                        'org.osgi.service.http.port': '8080',
                        'org.apache.felix.http.enabled': 'true',
                        'org.apache.felix.http.jettyEnabled': 'true',
                        'org.apache.felix.http.whiteboardEnabled': 'true',
                        'org.apache.felix.http.debug': 'true',
//                        'org.apache.felix.http.nio': 'false',
                    'felix.systembundle.activators': [/*
                            new DependencyActivatorBase() {
                                @Override
                                void init(BundleContext context, DependencyManager manager) throws Exception {
                                    manager.add(createComponent().setInterface(Object.name, null).setImplementation(new VersionInfo() {
                                        @Override
                                        @GET
                                        String getVersionString() {
                                            return '1.0'
                                        }
                                    }));
                                }

                                @Override
                                void destroy(BundleContext context, DependencyManager manager) throws Exception {

                                }
                            },    */
                            new BundleActivator() {
                                private ServiceRegistration registration;

                                public void start(BundleContext context) throws Exception {
                                    serviceLocator = [context]
                                    bundleContext = context
                                }

                                public void stop(BundleContext context) throws Exception {
                                    this.registration?.unregister();
                                }
                            }
                            /*,
                            new BundleActivator() {
                                @Override
                                void start(BundleContext context) throws Exception {
                                    context.registerService(Object, new VersionInfo() {
                                        @Override
                                        String getVersionString() {
                                            return '1.0'
                                        }
                                    }, null)
                                }

                                @Override
                                void stop(BundleContext context) throws Exception {
                                }
                            } */
                    ]]
            }
            bundles {
                [
                        // logging
                        'org.slf4j/slf4j-api/1.7.5/jar/6b262da268f8ad9eff941b25503a9198f0a0ac93/slf4j-api-1.7.5.jar',
                        'ch.qos.logback/logback-core/1.0.13/jar/dc6e6ce937347bd4d990fc89f4ceb469db53e45e/logback-core-1.0.13.jar',
                        'ch.qos.logback/logback-classic/1.0.13/jar/6b56ec752b42ccfa1415c0361fb54b1ed7ca3db6/logback-classic-1.0.13.jar',
                        'org.osgi/org.osgi.compendium/5.0.0/jar/9d7a9c35591f6fa1c98ac85af32775c12361aee4/org.osgi.compendium-5.0.0.jar',
                        'org.apache.felix/org.apache.felix.scr/1.8.2/bundle/c3047d56ee57de0752821fd9c3894dda664f2e37/org.apache.felix.scr-1.8.2.jar',
                        'org.apache.felix/org.apache.felix.dependencymanager/3.1.0/bundle/413a8b8507a3dd655cae768851989513ddea001a/org.apache.felix.dependencymanager-3.1.0.jar',
                        'org.apache.felix/org.apache.felix.log/1.0.1/bundle/af69a7c5c46be83a5527d4c146c69ce1c00b9e20/org.apache.felix.log-1.0.1.jar',
                        'org.codehaus.groovy/groovy-all/2.1.7/jar/c136ae67c3c40740ae986582baf65ba5c5ce69a0/groovy-all-2.1.7.jar',
                        'org.codehaus.gpars/gpars/1.1.0/jar/32b489ab3f4e7e5d8282189140591c791917f9cb/gpars-1.1.0.jar',
                        'org.fusesource.jansi/jansi/1.11/jar/655c643309c2f45a56a747fda70e3fadf57e9f11/jansi-1.11.jar',
//                        'org.apache.felix/org.apache.felix.http.jetty/2.2.2/jar/66f05113deb305be8ad1b80c538ad526cfc66a9c/org.apache.felix.http.jetty-2.2.2.jar',
//                        'org.apache.felix/org.apache.felix.http.whiteboard/2.2.2/jar/eea2890ee68432de5fd97d100ff6e4390205e3ea/org.apache.felix.http.whiteboard-2.2.2.jar',
//                        'org.apache.felix/org.apache.felix.http.bridge/2.2.2/jar/8ac73f724ed2b4a4c976da8bbd689afc6860e772/org.apache.felix.http.bridge-2.2.2.jar',
                        'org.apache.felix/org.apache.felix.http.bundle/2.2.2/jar/960b5718a4a8b5405fcbce7aa45066b2584efb65/org.apache.felix.http.bundle-2.2.2.jar',
                        // rest
                        'org.amdatu.web.rest.jaxrs/org.amdatu.web.rest.jaxrs/1.0.4/jar/6636897da9e0266cdbf54ca022cd0e72367d218b/org.amdatu.web.rest.jaxrs-1.0.4.jar',
                        'org.amdatu.web.rest.wink/org.amdatu.web.rest.wink/1.0.8/jar/c5f028cd045b6bce3ec47d86e2ecf1602ed3b865/org.amdatu.web.rest.wink-1.0.8.jar',
                        'org.codehaus.jackson/jackson-core-asl/1.9.13/jar/3c304d70f42f832e0a86d45bd437f692129299a4/jackson-core-asl-1.9.13.jar',
                        'org.codehaus.jackson/jackson-jaxrs/1.9.13/jar/534d72d2b9d6199dd531dfb27083dd4844082bba/jackson-jaxrs-1.9.13.jar',
                        'org.codehaus.jackson/jackson-mapper-asl/1.9.13/jar/1ee2f2bed0e5dd29d1cb155a166e6f8d50bbddb7/jackson-mapper-asl-1.9.13.jar'

                ].each {
                    start "file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/$it"
                }

                // figurate..
                [
                        'figurate-osgi/build/libs/figurate-osgi.jar',
                        'figurate-common/build/libs/figurate-common.jar',
                        'figurate-log/build/libs/figurate-log.jar'
                ].each {
                    start "file:///Users/fortuna/Development/figurate-core/modules/$it"
                }

            }
        }

        and:
        osgi.bundleContext.bundles.each {
            println "$it.bundleId $it.symbolicName $it.state"
        }

        and:
        evaluator = serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${ScriptEvaluator.canonicalName})"
            }
        })

        and:
        evaluator.evaluate(getClass().getResource('/scripts/RegisterServlet.groovy').text,
                ['context': bundleContext])

        and:
        evaluator.evaluate(getClass().getResource('/scripts/RegisterRestEndpoint.groovy').text,
                ['context': bundleContext])

        expect:
//        TimeUnit.SECONDS.sleep(30)
        new URL('http://localhost:8080/hello').text == 'Hello World'
        new URL('http://localhost:8080/version').text == '1.0'
    }
}
