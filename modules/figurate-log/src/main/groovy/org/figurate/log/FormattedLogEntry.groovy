package org.figurate.log

import groovy.transform.CompileStatic

import org.figurate.log.LogEntry.Level

@CompileStatic
class FormattedLogEntry implements LogEntry {

    Level level
    String template

    public String getMessage(Object...args) {
        return String.format(template, args)
    }
}
