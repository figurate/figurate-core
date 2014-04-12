package org.figurate.test

import org.figurate.osgi.FrameworkLauncher
import org.osgi.framework.launch.Framework
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by fortuna on 11/04/14.
 */
abstract class AbstractFrameworkSpec extends Specification {

    @Shared def config
    @Shared Framework osgi
    @Shared def binding = [:]

    void loadConfig(def configFile) {
        Binding binding = []
        GroovyShell shell = [binding]
        config = shell.evaluate(configFile)
    }

    def setup() {
        FrameworkLauncher launcher = [config: config]
        launcher.launch(binding)
        osgi = launcher.osgi
    }
}
