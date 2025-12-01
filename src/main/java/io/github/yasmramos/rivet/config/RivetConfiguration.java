package io.github.yasmramos.rivet.logging.config;

import io.github.yasmramos.rivet.logging.api.LogSink;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Configuration class for Rivet logging system.
 * Manages global settings, output sinks, and formatting options.
 */
public class RivetConfiguration {
    
    private LogLevel minimumLevel = LogLevel.INFO;
    private boolean prettyPrint = false;
    private boolean includeHostname = true;
    private boolean debugToConsole = true;
    private String applicationName;
    private String applicationVersion;
    private String environment;
    private ZoneId timezone = ZoneId.systemDefault();
    private final List<LogSink> sinks = new CopyOnWriteArrayList<>();
    
    // Default constructor
    public RivetConfiguration() {
        // Add default console sink
        sinks.add(new LogSink.ConsoleSink());
    }
    
    /**
     * Sets the minimum log level to enable.
     */
    public RivetConfiguration setMinLevel(LogLevel level) {
        this.minimumLevel = level;
        return this;
    }
    
    /**
     * Sets whether to format JSON output with pretty printing.
     */
    public RivetConfiguration setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }
    
    /**
     * Sets whether to include hostname in log entries.
     */
    public RivetConfiguration setIncludeHostname(boolean includeHostname) {
        this.includeHostname = includeHostname;
        return this;
    }
    
    /**
     * Sets whether to output debug logs to console.
     */
    public RivetConfiguration setDebugToConsole(boolean debugToConsole) {
        this.debugToConsole = debugToConsole;
        return this;
    }
    
    /**
     * Sets the application name.
     */
    public RivetConfiguration setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }
    
    /**
     * Sets the application version.
     */
    public RivetConfiguration setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
        return this;
    }
    
    /**
     * Sets the environment name (e.g., "development", "production").
     */
    public RivetConfiguration setEnvironment(String environment) {
        this.environment = environment;
        return this;
    }
    
    /**
     * Sets the timezone for timestamps.
     */
    public RivetConfiguration setTimezone(ZoneId timezone) {
        this.timezone = timezone;
        return this;
    }
    
    /**
     * Adds a log sink.
     */
    public RivetConfiguration addSink(LogSink sink) {
        if (sink != null) {
            sinks.add(sink);
        }
        return this;
    }
    
    /**
     * Removes a log sink.
     */
    public RivetConfiguration removeSink(LogSink sink) {
        sinks.remove(sink);
        return this;
    }
    
    /**
     * Clears all log sinks.
     */
    public RivetConfiguration clearSinks() {
        sinks.clear();
        return this;
    }
    
    // Getters
    public LogLevel getMinLevel() {
        return minimumLevel;
    }
    
    public boolean isPrettyPrint() {
        return prettyPrint;
    }
    
    public boolean isIncludeHostname() {
        return includeHostname;
    }
    
    public boolean isDebugToConsole() {
        return debugToConsole;
    }
    
    public String getApplicationName() {
        return applicationName;
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public ZoneId getTimezone() {
        return timezone;
    }
    
    public List<LogSink> getSinks() {
        return new ArrayList<>(sinks);
    }
    
    /**
     * Builder pattern for creating configurations.
     */
    public static class Builder {
        private RivetConfiguration config = new RivetConfiguration();
        
        public static Builder create() {
            return new Builder();
        }
        
        public Builder minLevel(LogLevel level) {
            config.setMinLevel(level);
            return this;
        }
        
        public Builder prettyPrint(boolean pretty) {
            config.setPrettyPrint(pretty);
            return this;
        }
        
        public Builder includeHostname(boolean include) {
            config.setIncludeHostname(include);
            return this;
        }
        
        public Builder debugToConsole(boolean debug) {
            config.setDebugToConsole(debug);
            return this;
        }
        
        public Builder applicationName(String name) {
            config.setApplicationName(name);
            return this;
        }
        
        public Builder applicationVersion(String version) {
            config.setApplicationVersion(version);
            return this;
        }
        
        public Builder environment(String env) {
            config.setEnvironment(env);
            return this;
        }
        
        public Builder timezone(ZoneId timezone) {
            config.setTimezone(timezone);
            return this;
        }
        
        public Builder addSink(LogSink sink) {
            config.addSink(sink);
            return this;
        }
        
        public RivetConfiguration build() {
            return config;
        }
    }
}