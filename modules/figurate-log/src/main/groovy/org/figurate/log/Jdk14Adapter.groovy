package org.figurate.log

import java.util.logging.Level
import java.util.logging.Logger

public class Jdk14Adapter extends AbstractLogAdapter {

    private final Logger logger
    
    public Jdk14Adapter(Logger logger) {
        this.logger = logger
    }

    @Override
    protected boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE)
    }

    @Override
    protected boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO)
    }

    @Override
    protected boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING)
    }

    @Override
    protected boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE)
    }

    @Override
    protected void debug(String message) {
        logger.log(Level.FINE, message)
    }
    
    @Override
    protected void debug(String message, Throwable e) {
        logger.log(Level.FINE, message, e)
    }
    
    @Override
    protected void info(String message) {
        logger.log(Level.INFO, message)
    }
    
    @Override
    protected void info(String message, Throwable e) {
        logger.log(Level.INFO, message, e)
    }
    
    @Override
    protected void warn(String message) {
        logger.log(Level.WARNING, message)
    }
    
    @Override
    protected void warn(String message, Throwable e) {
        logger.log(Level.WARNING, message, e)
    }
    
    @Override
    protected void error(String message) {
        logger.log(Level.SEVERE, message)
    }
    
    @Override
    protected void error(String message, Throwable e) {
        logger.log(Level.SEVERE, message, e)
    }
}
