package org.figurate.osgi

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.launch.Framework
import spock.lang.Specification

/**
 * Created by fortuna on 28/01/14.
 */
class FrameworkBuilderSpec extends Specification {

    def 'test build framework'() {
        setup:
        def osgi = new FrameworkBuilder().osgi {

            config {[
                'felix.systembundle.activators': [new BundleActivator() {
                    void start(BundleContext context) throws Exception {
                        println "System bundle started"
                    }
                    void stop(BundleContext context) throws Exception {
                        println "System bundle stopped"
                    }
                }]]
            }

            bundles {
                start 'file:///Users/fortuna/.gradle/caches/artifacts-26/filestore/org.codehaus.groovy/groovy-all/2.1.7/jar/c136ae67c3c40740ae986582baf65ba5c5ce69a0/groovy-all-2.1.7.jar'
            }
        }

        expect:
        osgi instanceof Framework
        osgi.bundleContext.bundles.each {
            println it.symbolicName
        }
    }
}
