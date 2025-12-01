package com.chronicle.logging.api;

import com.chronicle.logging.config.LogLevel;
import com.chronicle.logging.config.ChronicleConfiguration;
import com.chronicle.logging.core.LogEntry;
import com.chronicle.logging.util.JsonFormatter;
import com.chronicle.logging.util.TimestampProvider;
import com.chronicle.logging.util.ThreadContext;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Core logger implementation that handles JSON formatting and output.
 * Manages context, tags, and formatting for all log entries.
 */
public class ChronicleLogger {
    
    private final String name;
    private final ChronicleConfiguration configuration;
    private final JsonFormatter jsonFormatter;
    private final TimestampProvider timestampProvider;
    private final ThreadContext threadContext;
    
    public ChronicleLogger(String name, ChronicleConfiguration configuration) {
        this.name = name;
        this.configuration = configuration;
        this.jsonFormatter = new JsonFormatter(configuration);
        this.timestampProvider = new TimestampProvider(configuration.getTimezone());
        // Use the singleton thread context for proper context sharing
        this.threadContext = ThreadContext.get();
    }
    
    /**
     * Logs a message with the specified level and context.
     */
    public void log(LogLevel level, String message, Map<String, Object> context, 
                    Map<String, String> tags, Object[] args) {
        if (!isLevelEnabled(level)) {
            return;
        }
        
        LogEntry entry = createLogEntry(level, message, context, tags, args);
        formatAndOutput(entry);
    }
    
    /**
     * Logs a message with automatic context from ThreadContext.
     */
    public void log(LogLevel level, String message, Object[] args) {
        Map<String, Object> context = threadContext.getAll();
        Map<String, String> tags = threadContext.getTags();
        log(level, message, context, tags, args);
    }
    
    /**
     * Checks if the specified log level is enabled.
     */
    public boolean isLevelEnabled(LogLevel level) {
        return level.ordinal() >= configuration.getMinLevel().ordinal();
    }
    
    /**
     * Adds context data that will be included in subsequent log entries.
     */
    public void addContext(String key, Object value) {
        threadContext.put(key, value);
    }
    
    /**
     * Adds a tag that will be included in subsequent log entries.
     */
    public void addTag(String tag, String value) {
        threadContext.putTag(tag, value);
    }
    
    /**
     * Removes context data.
     */
    public void removeContext(String key) {
        threadContext.remove(key);
    }
    
    /**
     * Removes a tag.
     */
    public void removeTag(String tag) {
        threadContext.removeTag(tag);
    }
    
    /**
     * Gets the logger name.
     */
    public String getName() {
        return name;
    }
    
    private LogEntry createLogEntry(LogLevel level, String message, 
                                   Map<String, Object> context,
                                   Map<String, String> tags, 
                                   Object[] args) {
        Instant timestamp = timestampProvider.now();
        
        String formattedMessage = interpolateMessage(message, args);
        
        return new LogEntry.Builder()
            .timestamp(timestamp)
            .level(level)
            .loggerName(name)
            .message(formattedMessage)
            .context(context)
            .tags(tags)
            .threadId(Thread.currentThread().getId())
            .threadName(Thread.currentThread().getName())
            .build();
    }
    
    private String interpolateMessage(String message, Object[] args) {
        if (args == null || args.length == 0 || message == null) {
            return message;
        }
        
        // Simple message interpolation: replace {0}, {1}, etc. with args
        String result = message;
        for (int i = 0; i < args.length; i++) {
            String placeholder = "{" + i + "}";
            result = result.replace(placeholder, Objects.toString(args[i], "null"));
        }
        return result;
    }
    
    private void formatAndOutput(LogEntry entry) {
        try {
            String jsonLog = jsonFormatter.format(entry);
            
            // Output to configured sinks
            for (LogSink sink : configuration.getSinks()) {
                sink.write(jsonLog);
            }
            
            // Also output to stderr for development
            if (configuration.isDebugToConsole()) {
                System.err.println(jsonLog);
            }
        } catch (Exception e) {
            // Fallback logging in case of JSON formatting issues
            System.err.println("Chronicle logging error: " + e.getMessage());
        }
    }
}