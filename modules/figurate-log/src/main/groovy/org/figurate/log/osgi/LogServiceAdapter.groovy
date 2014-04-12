package org.figurate.log.osgi

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovyx.gpars.GParsExecutorsPool
import org.apache.felix.scr.annotations.*
import org.figurate.log.AnsiLogEntry
import org.figurate.log.AsyncLogAdapter
import org.figurate.log.LogAdapter
import org.figurate.log.LogEntry.Level
import org.figurate.log.Slf4jLogAdapter
import org.fusesource.jansi.AnsiConsole
import org.osgi.framework.Bundle
import org.osgi.service.log.LogEntry
import org.osgi.service.log.LogListener
import org.osgi.service.log.LogReaderService
import org.osgi.service.log.LogService
import org.slf4j.LoggerFactory

import static org.figurate.log.LogEntry.Level.*
import static org.fusesource.jansi.Ansi.Color.*

/**
 * Created by fortuna on 9/02/14.
 */
@CompileStatic
@Slf4j
@Component(immediate = true)
@Reference(referenceInterface = LogReaderService, bind = 'bind', unbind = 'unbind', cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
class LogServiceAdapter implements LogListener {

    private Map<Long, LogAdapter> logAdapters = [:]

//    private org.figurate.log.LogEntry debug = new AnsiLogEntry(level: Debug, template: '%s %s %s %s', colour: CYAN)
    private org.figurate.log.LogEntry debug = new BundleLogEntry(level: Debug)
    private org.figurate.log.LogEntry info = new BundleLogEntry(level: Info)
    private org.figurate.log.LogEntry warning = new BundleLogEntry(level: Warn)
    private org.figurate.log.LogEntry error = new BundleLogEntry(level: Level.Error)
//    private org.figurate.log.LogEntry warning = new AnsiLogEntry(level: Warn, template: '%s %s %s %s', colour: YELLOW)
//    private org.figurate.log.LogEntry error = new AnsiLogEntry(level: Level.Error, template: '%s %s %s %s', colour: RED)

    @Activate
    void activate() {
//        AnsiConsole.systemInstall()
    }

    @Deactivate
    void deactivate() {
//        AnsiConsole.systemUninstall()
    }

    void bind(LogReaderService logReaderService, Map<String, ?> properties) {
        logReaderService.addLogListener(this)
        log.debug 'Writing log backlog..'
        GParsExecutorsPool.withPool {
            GParsExecutorsPool.executeAsync {
                logReaderService.log.toList().reverseEach {
                    logged((LogEntry) it)
                }
            }
        }
        log.debug 'Writing log backlog complete.'
    }

    void unbind(LogReaderService logReaderService, Map<String, ?> properties) {
        logReaderService.removeLogListener(this)
    }

    @Override
    void logged(LogEntry entry) {
        if (!logAdapters[entry.bundle.bundleId]) {
            Slf4jLogAdapter logAdapter = [LoggerFactory.getLogger(entry.bundle.symbolicName)]
            logAdapters[entry.bundle.bundleId] = new AsyncLogAdapter(logAdapter)
        }
        org.figurate.log.LogEntry logEntry
        switch (entry.level) {
            case LogService.LOG_DEBUG: logEntry = debug; break
            case LogService.LOG_INFO: logEntry = info; break
            case LogService.LOG_WARNING: logEntry = warning; break
            case LogService.LOG_ERROR: logEntry = error; break
            default: logEntry = info
        }
        if (entry.exception) {
            logAdapters[entry.bundle.bundleId].log(logEntry, entry.exception, entry.message, entry.bundle,
                    entry.serviceReference, entry.time)
        }
        else {
            logAdapters[entry.bundle.bundleId].log(logEntry, entry.message, entry.bundle,
                    entry.serviceReference, entry.time)
        }
    }

    private static class BundleLogEntry implements org.figurate.log.LogEntry {

        private Level level

        @Override
        Level getLevel() {
            return level
        }

        @Override
        String getMessage(Object... args) {
            Bundle bundle = (Bundle) args[1]
            return "${bundle.bundleId} ${args[0]} ${args[2] ?: '[]'}, ${new Date((Long) args[3])}"
        }
    }
}
