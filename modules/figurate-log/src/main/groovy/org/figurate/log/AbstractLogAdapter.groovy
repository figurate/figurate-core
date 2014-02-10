package org.figurate.log

import groovy.transform.CompileStatic

import org.figurate.log.LogEntry.Level

@CompileStatic
abstract class AbstractLogAdapter implements LogAdapter {

    @Override
    final boolean isLoggable(LogEntry entry) {
        boolean loggable = false
        switch(entry.level) {
          case Level.Debug: loggable = isDebugEnabled(); break
          case Level.Info: loggable = isInfoEnabled(); break
          case Level.Warn: loggable = isWarnEnabled(); break
          case Level.Error: loggable = isErrorEnabled(); break
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
              case Level.Debug: debug(entry.getMessage(args)); break
              case Level.Info: info(entry.getMessage(args)); break
              case Level.Warn: warn(entry.getMessage(args)); break
              case Level.Error: error(entry.getMessage(args)); break
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
              case Level.Debug: debug(entry.getMessage(args), exception); break
              case Level.Info: info(entry.getMessage(args), exception); break
              case Level.Warn: warn(entry.getMessage(args), exception); break
              case Level.Error: error(entry.getMessage(args), exception); break
            }
        }
    }

    protected abstract void debug(String message, Throwable e)

    protected abstract void info(String message, Throwable e)

    protected abstract void warn(String message, Throwable e)

    protected abstract void error(String message, Throwable e)
}
