package com.chronicle.logging.core;

import com.chronicle.logging.api.RivetLogger;
import com.chronicle.logging.api.FluentLoggerBuilder;
import com.chronicle.logging.config.LogLevel;
import com.chronicle.logging.config.RivetConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main entry point for Rivet Logging Library.
 * Provides fluent API for logging with type-safe argument interpolation.
 * 
 * Example usage:
 * Rivet.info()
 *     .message("User {user} logged in")
 *     .arg("alice")
 *     .context("userId", "12345")
 *     .tag("security", "auth")
 *     .log();
 */
public class Rivet {
    
    private static final RivetConfiguration DEFAULT_CONFIG = new RivetConfiguration();
    private static final Rivet INSTANCE = new Rivet();
    
    private final RivetLogger logger;
    private final RivetConfiguration configuration;
    private final Map<String, RivetLogger> namedLoggers;
    
    private Rivet() {
        this.configuration = DEFAULT_CONFIG;
        this.logger = new RivetLogger("default", configuration);
        this.namedLoggers = new ConcurrentHashMap<>();
    }
    
    /**
     * Creates a new logger for the specified class.
     */
    public static RivetLogger forClass(Class<?> clazz) {
        return INSTANCE.createLogger(clazz.getName());
    }
    
    /**
     * Creates a new logger for the specified name.
     */
    public static RivetLogger getLogger(String name) {
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
    
    RivetLogger createLogger(String name) {
        return namedLoggers.computeIfAbsent(name, key -> 
            new RivetLogger(key, configuration));
    }
    
    public static RivetConfiguration getConfiguration() {
        return INSTANCE.configuration;
    }
}