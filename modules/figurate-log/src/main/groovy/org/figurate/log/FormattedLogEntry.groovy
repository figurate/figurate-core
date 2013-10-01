package org.figurate.log

class FormattedLogEntry implements LogEntry {

    Level level
    String template

    public String getMessage(Object...args) {
        return String.format(template, args)
    }
}
