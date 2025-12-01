package com.chronicle.logging.api;

import com.chronicle.logging.config.LogLevel;
import com.chronicle.logging.core.Rivet;
import com.chronicle.logging.util.ThreadContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fluent API builder for Rivet logging.
 * Implements the builder pattern for creating log entries with context, tags, and arguments.
 * 
 * Example usage:
 * Rivet.info()
 *     .message("User {0} logged in")
 *     .arg("alice")
 *     .context("userId", "12345")
 *     .tag("security", "auth")
 *     .log();
 */
public class FluentLoggerBuilder {
    
    private final LogLevel level;
    private final Rivet rivet;
    private final String message;
    private final List<Object> args;
    private final Map<String, Object> context;
    private final Map<String, String> tags;
    private final String loggerName;
    private final RivetLogger logger;
    
    public FluentLoggerBuilder(LogLevel level, Rivet rivet) {
        this(level, rivet, null, null);
    }
    
    public FluentLoggerBuilder(LogLevel level, Rivet rivet, String message, RivetLogger logger) {
        this.level = level;
        this.rivet = rivet;
        this.message = message;
        this.args = new ArrayList<>();
        this.context = new HashMap<>();
        this.tags = new HashMap<>();
        this.loggerName = null;
        this.logger = logger;
    }
    
    /**
     * Sets the logger name (optional, defaults to class name).
     */
    public FluentLoggerBuilder loggerName(String loggerName) {
        RivetLogger newLogger = Rivet.getLogger(loggerName);
        return new FluentLoggerBuilder(level, rivet, message, newLogger);
    }
    
    /**
     * Sets the log message with placeholders.
     * Use {0}, {1}, etc. for argument interpolation.
     */
    public FluentLoggerBuilder message(String message) {
        return new FluentLoggerBuilder(level, rivet, message, logger);
    }
    
    /**
     * Adds an argument for message interpolation.
     */
    public FluentLoggerBuilder arg(Object arg) {
        FluentLoggerBuilder newBuilder = new FluentLoggerBuilder(level, rivet, message, logger);
        newBuilder.args.addAll(this.args);
        newBuilder.args.add(arg);
        newBuilder.context.putAll(this.context);
        newBuilder.tags.putAll(this.tags);
        return newBuilder;
    }
    
    /**
     * Adds multiple arguments for message interpolation.
     */
    public FluentLoggerBuilder args(Object... args) {
        FluentLoggerBuilder newBuilder = new FluentLoggerBuilder(level, rivet, message, logger);
        newBuilder.args.addAll(this.args);
        newBuilder.args.addAll(Arrays.asList(args));
        newBuilder.context.putAll(this.context);
        newBuilder.tags.putAll(this.tags);
        return newBuilder;
    }
    
    /**
     * Adds a context key-value pair.
     */
    public FluentLoggerBuilder context(String key, Object value) {
        FluentLoggerBuilder newBuilder = new FluentLoggerBuilder(level, rivet, message, logger);
        newBuilder.args.addAll(this.args);
        newBuilder.context.putAll(this.context);
        newBuilder.context.put(key, value);
        newBuilder.tags.putAll(this.tags);
        return newBuilder;
    }
    
    /**
     * Adds multiple context key-value pairs.
     */
    public FluentLoggerBuilder context(Map<String, Object> context) {
        FluentLoggerBuilder newBuilder = new FluentLoggerBuilder(level, rivet, message, logger);
        newBuilder.args.addAll(this.args);
        newBuilder.context.putAll(this.context);
        newBuilder.context.putAll(context);
        newBuilder.tags.putAll(this.tags);
        return newBuilder;
    }
    
    /**
     * Adds a tag.
     */
    public FluentLoggerBuilder tag(String tag, String value) {
        FluentLoggerBuilder newBuilder = new FluentLoggerBuilder(level, rivet, message, logger);
        newBuilder.args.addAll(this.args);
        newBuilder.context.putAll(this.context);
        newBuilder.tags.putAll(this.tags);
        newBuilder.tags.put(tag, value);
        return newBuilder;
    }
    
    /**
     * Adds multiple tags.
     */
    public FluentLoggerBuilder tags(Map<String, String> tags) {
        FluentLoggerBuilder newBuilder = new FluentLoggerBuilder(level, rivet, message, logger);
        newBuilder.args.addAll(this.args);
        newBuilder.context.putAll(this.context);
        newBuilder.tags.putAll(this.tags);
        newBuilder.tags.putAll(tags);
        return newBuilder;
    }
    
    /**
     * Executes the log operation and outputs the log entry.
     */
    public void log() {
        if (message == null) {
            throw new IllegalStateException("Message cannot be null. Use .message() before .log()");
        }
        
        RivetLogger loggerToUse = logger != null ? logger : getDefaultLogger();
        loggerToUse.log(level, message, context, tags, args.toArray(new Object[0]));
    }
    
    /**
     * Creates a new RivetChapter for chapter-based logging.
     * This is the bridge between fluent API and chapter pattern.
     */
    public RivetChapter beginChapter(String chapterName) {
        return new RivetChapter(chapterName, getLogger(), level, message, args.toArray(new Object[0]), context, tags);
    }
    
    private RivetLogger getLogger() {
        if (logger != null) {
            return logger;
        }
        // Return a logger based on stack trace
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().equals(FluentLoggerBuilder.class.getName())) {
                String className = element.getClassName();
                try {
                    return Rivet.forClass(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    return Rivet.getLogger(className);
                }
            }
        }
        return Rivet.getLogger(FluentLoggerBuilder.class.getName());
    }
    
    private RivetLogger getDefaultLogger() {
        return getLogger();
    }
}