package org.figurate.test

import org.figurate.FrameworkLauncher
import org.osgi.framework.launch.Framework
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by fortuna on 11/04/14.
 */
abstract class AbstractFrameworkSpecification extends Specification {

    static {
        //for localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier(){

                    public boolean verify(String hostname,
                                          javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("localhost") || hostname.equals(Inet4Address.localHost.hostAddress)) {
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Shared def config
    @Shared Framework osgi
    @Shared def binding = [:]

    void loadConfig(def configFile) {
        Binding binding = []
        GroovyShell shell = [binding]
        config = shell.evaluate(configFile)
    }

    def setupSpec() {
        loadConfig(getConfigLocation())
        FrameworkLauncher launcher = [config: config]
        launcher.launch(binding)
        osgi = launcher.osgi
    }

    abstract getConfigLocation();
}
