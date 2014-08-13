package org.figurate

import groovy.util.logging.Slf4j
import org.osgi.framework.Bundle
import org.osgi.framework.launch.Framework
import org.osgi.framework.launch.FrameworkFactory
import org.osgi.framework.startlevel.BundleStartLevel
import org.osgi.framework.startlevel.FrameworkStartLevel
import org.osgi.framework.wiring.BundleRevision

/**
 * A builder to assist with the initialisation of an OSGi framework instance.
 *
 * Created by fortuna on 28/01/14.
 */
@Slf4j
class FrameworkBuilder {

    Framework framework

    private def configMap = [:]

    private boolean installBundleMode = false;
    private def installedBundles = []

    private def startLevelMap = [:]

    Framework osgi(Closure definition, def vars = []) {
        runClosure definition, vars

        def startLevels = startLevelMap.keySet().sort()

        // initialise bundle start levels..
        // Start level must be greater than zero.
        startLevels.findAll {it > 0}.each { startLevel ->
            startLevelMap[startLevel].each {
                def bundle = installedBundles.find {bundle -> bundle.symbolicName == it}
                if (bundle) {
                    bundle.adapt(BundleStartLevel).startLevel = startLevel
                } else {
                    log.warn "Bundle [$it] not found."
                }
            }
        }

//        GParsExecutorsPool.withPool {
            installedBundles.each { bundle ->
                // don't start the bundle if its start level is less than zero..
                boolean startBundle = !startLevelMap.find {
                    it.value.contains(bundle.symbolicName) && it.key < 0
                }
                // don't start fragment bundles, or bundles not in a resolved state..
                if (startBundle && (bundle.state == Bundle.RESOLVED || bundle.state == Bundle.INSTALLED)
                        && (bundle.adapt(BundleRevision.class).getTypes() & BundleRevision.TYPE_FRAGMENT) == 0) {
                    bundle.start()
                }
            }
//        }

        def slThread = Thread.start {
            // update start level..
            // Start level must be greater than zero.
            startLevels.findAll {it > 0}.each { startLevel ->
                def frameworkStartLevel = framework.adapt(FrameworkStartLevel)
                if (frameworkStartLevel.startLevel < startLevel) {
                    frameworkStartLevel.setStartLevel startLevel
                }
            }
        }
        slThread.join()

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

//        GParsExecutorsPool.withPool {
//            GParsExecutorsPool.executeAsync {
                framework.init()
                framework.start()
//            }
//        }
    }

    def bundles(Closure definition) {
        installBundleMode = true
        runClosure definition
        installBundleMode = false
    }

    def startLevels(Closure definition) {
        startLevelMap = runClosure definition
    }

    def start(String path) {
        if (installBundleMode) {
            def bundle
            try {
                new URL(path)
                bundle = framework.bundleContext.installBundle(path)
            } catch (MalformedURLException mue) {
                bundle = framework.bundleContext.installBundle(new File(System.properties['user.dir'], path).toURL() as String)
            }
            installedBundles << bundle
        }
    }
    /*
    def evaluate(BundleContext context, String expression) {
        OsgiServiceLocator serviceLocator = [context]
        def evaluator = serviceLocator.findService(new ServiceName() {
            @Override
            String getFilter() {
                return "(objectClass=${ScriptEvaluator.name})"
            }
        })
        evaluator.evaluate expression
    }
    */

    private runClosure(Closure closure, def vars = []) {
        Closure runClone = closure.clone()
        runClone.delegate = this
        runClone.resolveStrategy = Closure.DELEGATE_ONLY
        runClone(vars)
    }
}
