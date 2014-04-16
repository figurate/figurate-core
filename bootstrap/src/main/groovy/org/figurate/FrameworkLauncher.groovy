package org.figurate

import org.osgi.framework.launch.Framework

/**
 * Created by fortuna on 7/03/14.
 */
class FrameworkLauncher {

    def config
    Framework osgi

    void launch(def vars) {
        osgi = new FrameworkBuilder().osgi config, vars
    }

    static void main(def args) {
        FrameworkLauncher launcher = [config: new GroovyShell(FrameworkLauncher.classLoader).evaluate(new File(args[0]))]
        launcher.launch()
    }
}
