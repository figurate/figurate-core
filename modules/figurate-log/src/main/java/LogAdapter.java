package org.figurate.log;

public interface LogAdapter {
    
    void log(LogEntry entry, Object...args);

    void log(LogEntry entry, Throwable exception, Object...args);

    /**
     * @param entry the log entry
     * @return true if the log entry is loggable, otherwise false
     */
    boolean isLoggable(LogEntry entry);
}
