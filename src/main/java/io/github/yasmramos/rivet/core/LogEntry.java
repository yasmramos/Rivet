package io.github.yasmramos.rivet.logging.core;

import io.github.yasmramos.rivet.logging.config.LogLevel;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable log entry that contains all information for a single log operation.
 */
public class LogEntry {
    
    private final Instant timestamp;
    private final LogLevel level;
    private final String loggerName;
    private final String message;
    private final Map<String, Object> context;
    private final Map<String, String> tags;
    private final long threadId;
    private final String threadName;
    
    private LogEntry(Builder builder) {
        this.timestamp = builder.timestamp;
        this.level = builder.level;
        this.loggerName = builder.loggerName;
        this.message = builder.message;
        this.context = builder.context != null ? Map.copyOf(builder.context) : Map.of();
        this.tags = builder.tags != null ? Map.copyOf(builder.tags) : Map.of();
        this.threadId = builder.threadId;
        this.threadName = builder.threadName;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public LogLevel getLevel() {
        return level;
    }
    
    public String getLoggerName() {
        return loggerName;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Map<String, Object> getContext() {
        return context;
    }
    
    public Map<String, String> getTags() {
        return tags;
    }
    
    public long getThreadId() {
        return threadId;
    }
    
    public String getThreadName() {
        return threadName;
    }
    
    /**
     * Builder for LogEntry.
     */
    public static class Builder {
        private Instant timestamp;
        private LogLevel level;
        private String loggerName;
        private String message;
        private Map<String, Object> context;
        private Map<String, String> tags;
        private long threadId;
        private String threadName;
        
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder level(LogLevel level) {
            this.level = level;
            return this;
        }
        
        public Builder loggerName(String loggerName) {
            this.loggerName = loggerName;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder context(Map<String, Object> context) {
            this.context = context;
            return this;
        }
        
        public Builder tags(Map<String, String> tags) {
            this.tags = tags;
            return this;
        }
        
        public Builder threadId(long threadId) {
            this.threadId = threadId;
            return this;
        }
        
        public Builder threadName(String threadName) {
            this.threadName = threadName;
            return this;
        }
        
        public LogEntry build() {
            return new LogEntry(this);
        }
    }
}