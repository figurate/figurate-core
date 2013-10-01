package org.figurate.log;

/**
 * Defines a structured log message.
 */
public interface LogEntry {

    public enum Level {
        Trace, Debug, Info, Warn, Error;
    }
    
    Level getLevel();
    
    /**
     * @param args optional arguments to include in the message
     * @return a formatted log message
     */
    String getMessage(Object...args);
}
