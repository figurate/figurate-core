package org.figurate

import groovy.transform.CompileStatic
import org.osgi.framework.ServiceEvent
import org.osgi.framework.ServiceListener

import java.util.concurrent.CountDownLatch

/**
 * A helper class for tracking the availability of a service.
 */
@CompileStatic
class ServiceStatusListener implements ServiceListener {

    CountDownLatch serviceAvailable = [1]

    @Override
    void serviceChanged(ServiceEvent event) {
        if (event.type == ServiceEvent.REGISTERED) {
            serviceAvailable.countDown()
        }
    }
}
