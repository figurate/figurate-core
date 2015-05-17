package org.figurate.test

import groovy.util.logging.Slf4j
import org.figurate.FrameworkLauncher
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.TimeUnit

/**
 * Created by fortuna on 11/04/14.
 */
@Slf4j
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

        System.setProperty('logback.configurationFile', 'build/config/logback.groovy')
    }

    @Shared def config
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
    }

    def setup() {
        // wait for framework to start fully before running tests.
        binding.frameworkStarted.await(30, TimeUnit.SECONDS)
    }

    abstract getConfigLocation();
}
