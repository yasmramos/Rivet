package io.github.yasmramos.rivet.logging.config;

/**
 * Log levels supported by Rivet.
 * Ordered from most verbose (TRACE) to most critical (ERROR).
 */
public enum LogLevel {
    
    /**
     * Trace level - most verbose logging for debugging.
     * Typically used for extremely detailed debugging information.
     */
    TRACE(0),
    
    /**
     * Debug level - detailed information for debugging purposes.
     * Used for debugging application flow and state.
     */
    DEBUG(1),
    
    /**
     * Info level - general informational messages.
     * Used for highlighting the progress of the application.
     */
    INFO(2),
    
    /**
     * Warn level - warning messages about potentially harmful situations.
     * Used for unusual events that are not errors but should be noted.
     */
    WARN(3),
    
    /**
     * Error level - error messages for errors that are recoverable.
     * Used for error conditions that should be recovered from.
     */
    ERROR(4);
    
    private final int level;
    
    LogLevel(int level) {
        this.level = level;
    }
    
    /**
     * Gets the numeric value of this log level.
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Checks if this level is enabled given a minimum level.
     */
    public boolean isEnabled(LogLevel minimumLevel) {
        return this.level >= minimumLevel.level;
    }
    
    /**
     * Checks if this level is higher than or equal to another level.
     */
    public boolean isAtLeast(LogLevel other) {
        return this.level >= other.level;
    }
    
    /**
     * Checks if this level is lower than or equal to another level.
     */
    public boolean isAtMost(LogLevel other) {
        return this.level <= other.level;
    }
    
    /**
     * Gets a LogLevel from a string representation.
     */
    public static LogLevel fromString(String level) {
        if (level == null) {
            return INFO;
        }
        
        String normalized = level.toUpperCase().trim();
        
        switch (normalized) {
            case "TRACE":
            case "TR":
                return TRACE;
            case "DEBUG":
            case "DBG":
                return DEBUG;
            case "INFO":
            case "IN":
                return INFO;
            case "WARN":
            case "WARNING":
            case "WRN":
                return WARN;
            case "ERROR":
            case "ERR":
                return ERROR;
            default:
                throw new IllegalArgumentException("Unknown log level: " + level);
        }
    }
    
    /**
     * Gets the String representation of this level.
     */
    @Override
    public String toString() {
        return name();
    }
}