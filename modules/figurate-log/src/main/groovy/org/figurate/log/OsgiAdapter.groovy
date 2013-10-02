package org.figurate.log

import groovy.transform.CompileStatic

import org.osgi.service.log.LogService;

@CompileStatic
class OsgiAdapter extends AbstractLogAdapter {

    private final LogService logger
    
    public OsgiAdapter(LogService logger) {
        this.logger = logger
    }
    
    @Override
    protected void debug(String message) {
        logger.log(LogService.LOG_DEBUG, message)
    }

    @Override
    protected void debug(String message, Throwable e) {
        logger.log(LogService.LOG_DEBUG, message, e)
    }

    @Override
    protected void error(String message) {
        logger.log(LogService.LOG_ERROR, message)
    }

    @Override
    protected void error(String message, Throwable e) {
        logger.log(LogService.LOG_ERROR, message, e)
    }

    @Override
    protected void info(String message) {
        logger.log(LogService.LOG_INFO, message)
    }

    @Override
    protected void info(String message, Throwable e) {
        logger.log(LogService.LOG_INFO, message, e)
    }

    @Override
    protected boolean isDebugEnabled() { true }

    @Override
    protected boolean isErrorEnabled() { true }

    @Override
    protected boolean isInfoEnabled() { true }

    @Override
    protected boolean isWarnEnabled() { true }

    @Override
    protected void warn(String message) {
        logger.log(LogService.LOG_WARNING, message)
    }

    @Override
    protected void warn(String message, Throwable e) {
        logger.log(LogService.LOG_WARNING, message, e)
    }
}
