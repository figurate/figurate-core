package org.figurate.osgi

import org.osgi.framework.launch.Framework
import org.osgi.framework.launch.FrameworkFactory

/**
 * A builder to assist with the initialisation of an OSGi framework instance.
 *
 * Created by fortuna on 28/01/14.
 */
class FrameworkBuilder {

    Framework framework

    private def configMap = [:]

    private boolean installBundleMode = false;
    private def installedBundles = []

    Framework osgi(Closure definition) {
        runClosure definition
        installedBundles.each { bundle ->
            bundle.start()
        }
        framework
    }

    def config(Closure definition) {
        FrameworkFactory osgiFactory = ServiceLoader.load(FrameworkFactory).find()
        configMap = runClosure definition
        framework = osgiFactory.newFramework configMap

        System.addShutdownHook {
//            println "Shutting down"
            try {
                framework.stop()
                framework.waitForStop(0)
            }
            catch (Exception e) {
                e.printStackTrace()
            }
        }

        framework.init()
        framework.start()
    }

    def bundles(Closure definition) {
        installBundleMode = true
        runClosure definition
        installBundleMode = false
    }

    def start(String path) {
        if (installBundleMode) {
            installedBundles << framework.bundleContext.installBundle(path)
        }
    }

    private runClosure(Closure closure) {
        Closure runClone = closure.clone()
        runClone.delegate = this
        runClone.resolveStrategy = Closure.DELEGATE_ONLY
        runClone()
    }
}
