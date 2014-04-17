package scripts

import org.figurate.log.FormattedLogEntry

/*
 * This script demonstrates how groovy code can be injected into an OSGi runtime.
 */
import org.figurate.log.LogEntry

def logEntry = new FormattedLogEntry(level: LogEntry.Level.Debug, template: '%s')
adapter.log(logEntry, 'Test message')