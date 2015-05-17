package org.figurate

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.osgi.framework.FrameworkEvent
import org.osgi.framework.FrameworkListener

import java.util.concurrent.CountDownLatch

/**
 * A helper class for tracking the startup status of an OSGi framework.
 */
@Slf4j
@CompileStatic
class FrameworkStatusListener implements FrameworkListener {

    CountDownLatch frameworkStarted = [1]

    @Override
    void frameworkEvent(FrameworkEvent event) {
        if (event.type == FrameworkEvent.STARTED) {
            log.info "### Framework started. ###"
            frameworkStarted.countDown()
        } else if (event.type == FrameworkEvent.ERROR) {
            log.error '### Error starting framework ###', event.throwable
        } else {
            log.info "### $event.type, $event.bundle: $event.throwable ###"
        }
    }
}
