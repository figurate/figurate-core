package org.figurate.log

import groovy.transform.CompileStatic

import org.slf4j.Logger

@CompileStatic
public class Slf4jLogAdapter extends AbstractLogAdapter {

    private final Logger logger
    
    public Slf4jLogAdapter(Logger logger) {
        this.logger = logger
    }

    @Override
    protected boolean isDebugEnabled() {
        logger.debugEnabled
    }

    @Override
    protected boolean isInfoEnabled() {
        logger.infoEnabled
    }

    @Override
    protected boolean isWarnEnabled() {
        logger.warnEnabled
    }

    @Override
    protected boolean isErrorEnabled() {
        logger.errorEnabled
    }

    @Override
    protected void debug(String message) {
        logger.debug(message)
    }
    
    @Override
    protected void debug(String message, Throwable e) {
        logger.debug(message, e)
    }
    
    @Override
    protected void info(String message) {
        logger.info(message)
    }
    
    @Override
    protected void info(String message, Throwable e) {
        logger.info(message, e)
    }
    
    @Override
    protected void warn(String message) {
        logger.warn(message)
    }
    
    @Override
    protected void warn(String message, Throwable e) {
        logger.warn(message, e)
    }
    
    @Override
    protected void error(String message) {
        logger.error(message)
    }
    
    @Override
    protected void error(String message, Throwable e) {
        logger.error(message, e)
    }
}
