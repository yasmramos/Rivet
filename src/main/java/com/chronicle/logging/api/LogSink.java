package com.chronicle.logging.api;

/**
 * Interface for log output destinations.
 * Implementations write log entries to different targets (console, file, network, etc.).
 */
public interface LogSink {
    
    /**
     * Writes a log entry to this sink.
     * 
     * @param logEntry The formatted log entry to write
     */
    void write(String logEntry);
    
    /**
     * Flushes any buffered output.
     */
    void flush();
    
    /**
     * Closes this sink and releases any resources.
     */
    void close();
    
    /**
     * Gets the name of this sink.
     */
    String getName();
    
    /**
     * Console sink that writes to System.out/System.err.
     */
    class ConsoleSink implements LogSink {
        
        private final String name;
        private final boolean useErrorStream;
        
        public ConsoleSink() {
            this("console", false);
        }
        
        public ConsoleSink(boolean useErrorStream) {
            this("console", useErrorStream);
        }
        
        public ConsoleSink(String name, boolean useErrorStream) {
            this.name = name;
            this.useErrorStream = useErrorStream;
        }
        
        @Override
        public void write(String logEntry) {
            if (useErrorStream) {
                System.err.println(logEntry);
            } else {
                System.out.println(logEntry);
            }
        }
        
        @Override
        public void flush() {
            if (useErrorStream) {
                System.err.flush();
            } else {
                System.out.flush();
            }
        }
        
        @Override
        public void close() {
            // No-op for console
        }
        
        @Override
        public String getName() {
            return name;
        }
    }
    
    /**
     * Null sink that discards all log entries.
     */
    class NullSink implements LogSink {
        
        private final String name;
        
        public NullSink() {
            this("null");
        }
        
        public NullSink(String name) {
            this.name = name;
        }
        
        @Override
        public void write(String logEntry) {
            // Discard all log entries
        }
        
        @Override
        public void flush() {
            // No-op
        }
        
        @Override
        public void close() {
            // No-op
        }
        
        @Override
        public String getName() {
            return name;
        }
    }
}