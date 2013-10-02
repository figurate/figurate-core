package org.figurate.log

import groovy.transform.CompileStatic

import org.figurate.log.LogEntry.Level

@CompileStatic
abstract class AbstractLogAdapter implements LogAdapter {

    @Override
    final boolean isLoggable(LogEntry entry) {
        boolean loggable
        switch(entry.level) {
          case Level.Debug: loggable = isDebugEnabled()
          case Level.Info: loggable = isInfoEnabled()
          case Level.Warn: loggable = isWarnEnabled()
          case Level.Error: loggable = isErrorEnabled()
          default: loggable = false
        }
        return loggable;
    }

    protected abstract boolean isDebugEnabled()

    protected abstract boolean isInfoEnabled()

    protected abstract boolean isWarnEnabled()

    protected abstract boolean isErrorEnabled()
    
    @Override
    final void log(LogEntry entry, Object... args) {
        if (isLoggable(entry)) {
            switch (entry.level) {
              case Level.Debug: debug(entry.getMessage(args))
              case Level.Info: info(entry.getMessage(args))
              case Level.Warn: warn(entry.getMessage(args))
              case Level.Error: error(entry.getMessage(args))
            }
        }
    }

    protected abstract void debug(String message)

    protected abstract void info(String message)

    protected abstract void warn(String message)

    protected abstract void error(String message)

    @Override    
    final void log(LogEntry entry, Throwable exception, Object... args) {
        if (isLoggable(entry)) {
            switch (entry.level) {
              case Level.Debug: debug(entry.getMessage(args), exception)
              case Level.Info: info(entry.getMessage(args), exception)
              case Level.Warn: warn(entry.getMessage(args), exception)
              case Level.Error: error(entry.getMessage(args), exception)
            }
        }
    }

    protected abstract void debug(String message, Throwable e)

    protected abstract void info(String message, Throwable e)

    protected abstract void warn(String message, Throwable e)

    protected abstract void error(String message, Throwable e)
}
