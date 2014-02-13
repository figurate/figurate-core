package org.figurate.test

import org.figurate.ScriptEvaluator
import org.figurate.log.LogAdapter
import org.figurate.osgi.FrameworkBuilder
import org.figurate.osgi.OsgiServiceLocator
import org.figurate.osgi.ServiceName
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.launch.Framework
import spock.lang.Specification

/**
 * Created by fortuna on 30/01/14.
 */
class OsgiAdapterSpec extends Specification {

    OsgiServiceLocator serviceLocator
    def adapter
    def evaluator

//    @Ignore
    def 'test logging'() {
        setup: 'Initialise framework'
        Framework osgi = new FrameworkBuilder().osgi {
            config {[
                'org.osgi.framework.storage': 'build/felix-cache',
                'org.osgi.framework.storage.clean': 'onFirstInit',
                'felix.systembundle.activators': [
                    new BundleActivator() {
                        @Override
                        void start(BundleContext context) throws Exception {
                            serviceLocator = [context]
                        }

                        @Override
                        void stop(BundleContext context) throws Exception {
                        }
                    }
            ]]}

            bundles {
                [
                        'org.apache.felix/org.apache.felix.log/1.0.1/bundle/af69a7c5c46be83a5527d4c146c69ce1c00b9e20/org.apache.felix.log-1.0.1.jar',
                        // logging
                        'org.slf4j/slf4j-api/1.7.5/jar/6b262da268f8ad9eff941b25503a9198f0a0ac93/slf4j-api-1.7.5.jar',
                        'ch.qos.logback/logback-core/1.0.13/jar/dc6e6ce937347bd4d990fc89f4ceb469db53e45e/logback-core-1.0.13.jar',
                        'ch.qos.logback/logback-classic/1.0.13/jar/6b56ec752b42ccfa1415c0361fb54b1ed7ca3db6/logback-classic-1.0.13.jar',
                        // scr
                        'org.apache.felix/org.apache.felix.scr/1.8.2/bundle/c3047d56ee57de0752821fd9c3894dda664f2e37/org.apache.felix.scr-1.8.2.jar',
                        'org.codehaus.groovy/groovy-all/2.1.7/jar/c136ae67c3c40740ae986582baf65ba5c5ce69a0/groovy-all-2.1.7.jar',
                        'org.fusesource.jansi/jansi/1.11/jar/655c643309c2f45a56a747fda70e3fadf57e9f11/jansi-1.11.jar'
                ].each {
                    start "file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/$it"
                }
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.osgi/org.osgi.compendium/5.0.0/jar/9d7a9c35591f6fa1c98ac85af32775c12361aee4/org.osgi.compendium-5.0.0.jar'

                // blueprint..
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/asm/asm-all/3.2/jar/c55c18044017feac019374a5ae3d33bd8b277972/asm-all-3.2.jar'
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.apache.xbean/xbean-finder/3.7/bundle/379d42b37a0b7eea8f4437504f903fb4687cdb1/xbean-finder-3.7.jar'
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.apache.aries/org.apache.aries.util/1.0.0/bundle/ab4f0c672f653955f64cf40431a15fe2a49d9c33/org.apache.aries.util-1.0.0.jar'
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.apache.aries.proxy/org.apache.aries.proxy.api/1.0.0/bundle/6ffbb2b60734f6c5bd9f35293d0f9329583c9840/org.apache.aries.proxy.api-1.0.0.jar'
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.apache.aries.proxy/org.apache.aries.proxy.impl/1.0.2/bundle/cba80fec02631302cafa509e53dbbfd7326d6bd2/org.apache.aries.proxy.impl-1.0.2.jar'
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.apache.aries.blueprint/org.apache.aries.blueprint/1.1.0/bundle/667365387b9fcfb97edb76e68c9854abf36cf46b/org.apache.aries.blueprint-1.1.0.jar'
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.apache.aries.blueprint/org.apache.aries.blueprint.annotation.api/1.0.0/bundle/de75d385c7c362885564bb60266050d2513f028a/org.apache.aries.blueprint.annotation.api-1.0.0.jar'
//                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.apache.aries.blueprint/org.apache.aries.blueprint.annotation.impl/1.0.0/bundle/ea8dcef5caa423e5804f9dcb425dfc7d4ffc5731/org.apache.aries.blueprint.annotation.impl-1.0.0.jar'

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
        adapter = serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${LogAdapter.canonicalName})"
            }
        })

        and:
        evaluator = serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${ScriptEvaluator.canonicalName})"
            }
        })

        and:
        evaluator.evaluate(getClass().getResource('/scripts/LogTestMessage.groovy').text,
                ['adapter': adapter])
    }
}
