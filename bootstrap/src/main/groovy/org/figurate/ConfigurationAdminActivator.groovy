package org.figurate

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.service.cm.ConfigurationAdmin
import org.osgi.util.tracker.ServiceTracker

/**
 * Registers all configuration and factory configuration with configuration admin service.
 */
class ConfigurationAdminActivator implements BundleActivator {
    @Override
    void start(BundleContext context) throws Exception {
        ServiceTracker<ConfigurationAdmin, ?> cmTracker = [context, ConfigurationAdmin, null]
        cmTracker.open()
        ConfigurationAdmin configurationAdmin = cmTracker.service
        cmTracker.close()

        def configurationFile = System.properties['configurationAdmin.configurationFile']
        if (configurationFile) {
            def configurations = new GroovyShell(ConfigurationAdminActivator.classLoader).evaluate(new File(configurationFile))
            configurations.configProps.each { configuration ->
                def config = configurationAdmin.getConfiguration(configuration.key, null)
                def props = config.properties
                if (!props) {
                    props = new Hashtable()
                }
                props.putAll configuration.value

                log.info "Updating $config.pid: $props"
                config.update(props)
            }

            configurations.factoryConfigProps.each { configuration ->
                def config = configurationAdmin.createFactoryConfiguration(configuration.key, null)
                def props = config.properties
                if (!props) {
                    props = new Hashtable()
                }
                props.putAll configuration.value

                log.info "Updating $config.pid: $props"
                config.update(props)
            }
        }
    }

    @Override
    void stop(BundleContext context) throws Exception {

    }
}
