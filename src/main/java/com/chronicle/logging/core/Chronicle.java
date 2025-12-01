package com.chronicle.logging.core;

import com.chronicle.logging.api.ChronicleLogger;
import com.chronicle.logging.api.FluentLoggerBuilder;
import com.chronicle.logging.config.LogLevel;
import com.chronicle.logging.config.ChronicleConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main entry point for Chronicle Logging Library.
 * Provides fluent API for logging with type-safe argument interpolation.
 * 
 * Example usage:
 * Chronicle.info()
 *     .message("User {user} logged in")
 *     .arg("alice")
 *     .context("userId", "12345")
 *     .tag("security", "auth")
 *     .log();
 */
public class Chronicle {
    
    private static final ChronicleConfiguration DEFAULT_CONFIG = new ChronicleConfiguration();
    private static final Chronicle INSTANCE = new Chronicle();
    
    private final ChronicleLogger logger;
    private final ChronicleConfiguration configuration;
    private final Map<String, ChronicleLogger> namedLoggers;
    
    private Chronicle() {
        this.configuration = DEFAULT_CONFIG;
        this.logger = new ChronicleLogger("default", configuration);
        this.namedLoggers = new ConcurrentHashMap<>();
    }
    
    /**
     * Creates a new logger for the specified class.
     */
    public static ChronicleLogger forClass(Class<?> clazz) {
        return INSTANCE.createLogger(clazz.getName());
    }
    
    /**
     * Creates a new logger for the specified name.
     */
    public static ChronicleLogger getLogger(String name) {
        return INSTANCE.createLogger(name);
    }
    
    /**
     * Entry point for fluent API - creates an info level logger.
     */
    public static FluentLoggerBuilder info() {
        return new FluentLoggerBuilder(LogLevel.INFO, INSTANCE);
    }
    
    /**
     * Entry point for fluent API - creates a debug level logger.
     */
    public static FluentLoggerBuilder debug() {
        return new FluentLoggerBuilder(LogLevel.DEBUG, INSTANCE);
    }
    
    /**
     * Entry point for fluent API - creates a warn level logger.
     */
    public static FluentLoggerBuilder warn() {
        return new FluentLoggerBuilder(LogLevel.WARN, INSTANCE);
    }
    
    /**
     * Entry point for fluent API - creates an error level logger.
     */
    public static FluentLoggerBuilder error() {
        return new FluentLoggerBuilder(LogLevel.ERROR, INSTANCE);
    }
    
    /**
     * Entry point for fluent API - creates a trace level logger.
     */
    public static FluentLoggerBuilder trace() {
        return new FluentLoggerBuilder(LogLevel.TRACE, INSTANCE);
    }
    
    ChronicleLogger createLogger(String name) {
        return namedLoggers.computeIfAbsent(name, key -> 
            new ChronicleLogger(key, configuration));
    }
    
    public static ChronicleConfiguration getConfiguration() {
        return INSTANCE.configuration;
    }
}