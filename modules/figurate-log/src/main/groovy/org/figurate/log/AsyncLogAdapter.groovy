package org.figurate.log

import groovy.transform.CompileStatic

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

@CompileStatic
class AsyncLogAdapter implements LogAdapter {

    private static final BlockingQueue<Log> QUEUE = new LinkedBlockingQueue<Log>()
    
    private static final Thread QUEUE_PROCESSOR = [new QueueProcessor(), "AsyncLogAdapter"]
    static {
        QUEUE_PROCESSOR.start()
    }
    
    private final LogAdapter delegate
    
    AsyncLogAdapter(LogAdapter delegate) {
        this.delegate = delegate
    }
    
    @Override
    boolean isLoggable(LogEntry entry) {
        return delegate.isLoggable(entry)
    }

    @Override
    void log(LogEntry entry, Object... args) {
        QUEUE.add(new Log(delegate, entry, null, args))
    }

    @Override
    void log(LogEntry entry, Throwable exception, Object... args) {
        QUEUE.add(new Log(delegate, entry, exception, args))
    }

    private static class Log {
        
        private final LogAdapter logAdapter
        private final LogEntry logEntry
        private final Throwable exception
        private final Object[] args
        
        Log(LogAdapter logAdapter, LogEntry logEntry, Throwable exception, Object...args) {
            this.logAdapter = logAdapter
            this.logEntry = logEntry
            this.exception = exception
            this.args = args
        }
    }
    
    private static class QueueProcessor implements Runnable {
        
        private transient boolean shuttingDown

        @Override        
        public void run() {
            System.addShutdownHook {
                shuttingDown = true
                try {
                    QUEUE_PROCESSOR.join(1000)
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
            
            while (!shuttingDown || !QUEUE.isEmpty()) {
                try {
                    Log log = QUEUE.take()
                    if (log.exception != null) {
                        log.logAdapter.log(log.logEntry, log.exception, log.args)
                    }
                    else {
                        log.logAdapter.log(log.logEntry, log.args)
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
    }
}
