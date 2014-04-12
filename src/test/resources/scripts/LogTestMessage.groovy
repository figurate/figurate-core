/*
 * This script demonstrates how groovy code can be injected into an OSGi runtime.
 */
import org.figurate.log.FormattedLogEntry
import org.figurate.log.LogAdapter
import org.figurate.log.LogEntry
import org.figurate.osgi.OsgiServiceLocator

def logEntry = new FormattedLogEntry(level: LogEntry.Level.Debug, template: '%s')
adapter.log(logEntry, 'Test message')