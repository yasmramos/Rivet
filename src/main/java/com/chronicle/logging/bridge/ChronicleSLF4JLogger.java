package com.chronicle.logging.bridge;

import com.chronicle.logging.api.ChronicleLogger;
import com.chronicle.logging.config.LogLevel;
import com.chronicle.logging.core.Chronicle;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

/**
 * Chronicle implementation of SLF4J Logger interface.
 * Allows Chronicle to be used as a backend for SLF4J.
 * 
 * Example usage:
 * Logger slf4jLogger = ChronicleSLF4JLogger.getLogger(MyClass.class);
 * slf4jLogger.info("Usando Chronicle por detrás");
 */
public class ChronicleSLF4JLogger implements Logger {
    
    private final ChronicleLogger chronicleLogger;
    private final String name;
    
    ChronicleSLF4JLogger(String name) {
        this.name = name;
        this.chronicleLogger = Chronicle.getLogger(name);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean isTraceEnabled() {
        return chronicleLogger.isLevelEnabled(LogLevel.TRACE);
    }
    
    @Override
    public boolean isTraceEnabled(Marker marker) {
        return isTraceEnabled();
    }
    
    @Override
    public void trace(String msg) {
        log(LogLevel.TRACE, msg, null, null, null, null);
    }
    
    @Override
    public void trace(String format, Object arg) {
        log(LogLevel.TRACE, format, arg, null, null, null);
    }
    
    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(LogLevel.TRACE, format, arg1, arg2, null, null);
    }
    
    @Override
    public void trace(String format, Object... arguments) {
        log(LogLevel.TRACE, format, null, null, null, arguments);
    }
    
    @Override
    public void trace(String msg, Throwable t) {
        log(LogLevel.TRACE, msg, null, t, null, null);
    }
    
    @Override
    public void trace(Marker marker, String msg) {
        log(LogLevel.TRACE, msg, marker, null, null, null);
    }
    
    @Override
    public void trace(Marker marker, String format, Object arg) {
        log(LogLevel.TRACE, format, arg, marker, null, null);
    }
    
    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        log(LogLevel.TRACE, format, arg1, arg2, marker, null);
    }
    
    @Override
    public void trace(Marker marker, String format, Object... arguments) {
        log(LogLevel.TRACE, format, marker, null, null, arguments);
    }
    
    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        log(LogLevel.TRACE, msg, marker, t, null, null);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return chronicleLogger.isLevelEnabled(LogLevel.DEBUG);
    }
    
    @Override
    public boolean isDebugEnabled(Marker marker) {
        return isDebugEnabled();
    }
    
    @Override
    public void debug(String msg) {
        log(LogLevel.DEBUG, msg, null, null, null, null);
    }
    
    @Override
    public void debug(String format, Object arg) {
        log(LogLevel.DEBUG, format, arg, null, null, null);
    }
    
    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(LogLevel.DEBUG, format, arg1, arg2, null, null);
    }
    
    @Override
    public void debug(String format, Object... arguments) {
        log(LogLevel.DEBUG, format, null, null, null, arguments);
    }
    
    @Override
    public void debug(String msg, Throwable t) {
        log(LogLevel.DEBUG, msg, null, t, null, null);
    }
    
    @Override
    public void debug(Marker marker, String msg) {
        log(LogLevel.DEBUG, msg, marker, null, null, null);
    }
    
    @Override
    public void debug(Marker marker, String format, Object arg) {
        log(LogLevel.DEBUG, format, arg, marker, null, null);
    }
    
    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        log(LogLevel.DEBUG, format, arg1, arg2, marker, null);
    }
    
    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        log(LogLevel.DEBUG, format, marker, null, null, arguments);
    }
    
    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        log(LogLevel.DEBUG, msg, marker, t, null, null);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return chronicleLogger.isLevelEnabled(LogLevel.INFO);
    }
    
    @Override
    public boolean isInfoEnabled(Marker marker) {
        return isInfoEnabled();
    }
    
    @Override
    public void info(String msg) {
        log(LogLevel.INFO, msg, null, null, null, null);
    }
    
    @Override
    public void info(String format, Object arg) {
        log(LogLevel.INFO, format, arg, null, null, null);
    }
    
    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(LogLevel.INFO, format, arg1, arg2, null, null);
    }
    
    @Override
    public void info(String format, Object... arguments) {
        log(LogLevel.INFO, format, null, null, null, arguments);
    }
    
    @Override
    public void info(String msg, Throwable t) {
        log(LogLevel.INFO, msg, null, t, null, null);
    }
    
    @Override
    public void info(Marker marker, String msg) {
        log(LogLevel.INFO, msg, marker, null, null, null);
    }
    
    @Override
    public void info(Marker marker, String format, Object arg) {
        log(LogLevel.INFO, format, arg, marker, null, null);
    }
    
    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        log(LogLevel.INFO, format, arg1, arg2, marker, null);
    }
    
    @Override
    public void info(Marker marker, String format, Object... arguments) {
        log(LogLevel.INFO, format, marker, null, null, arguments);
    }
    
    @Override
    public void info(Marker marker, String msg, Throwable t) {
        log(LogLevel.INFO, msg, marker, t, null, null);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return chronicleLogger.isLevelEnabled(LogLevel.WARN);
    }
    
    @Override
    public boolean isWarnEnabled(Marker marker) {
        return isWarnEnabled();
    }
    
    @Override
    public void warn(String msg) {
        log(LogLevel.WARN, msg, null, null, null, null);
    }
    
    @Override
    public void warn(String format, Object arg) {
        log(LogLevel.WARN, format, arg, null, null, null);
    }
    
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(LogLevel.WARN, format, arg1, arg2, null, null);
    }
    
    @Override
    public void warn(String format, Object... arguments) {
        log(LogLevel.WARN, format, null, null, null, arguments);
    }
    
    @Override
    public void warn(String msg, Throwable t) {
        log(LogLevel.WARN, msg, null, t, null, null);
    }
    
    @Override
    public void warn(Marker marker, String msg) {
        log(LogLevel.WARN, msg, marker, null, null, null);
    }
    
    @Override
    public void warn(Marker marker, String format, Object arg) {
        log(LogLevel.WARN, format, arg, marker, null, null);
    }
    
    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        log(LogLevel.WARN, format, arg1, arg2, marker, null);
    }
    
    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        log(LogLevel.WARN, format, marker, null, null, arguments);
    }
    
    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        log(LogLevel.WARN, msg, marker, t, null, null);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return chronicleLogger.isLevelEnabled(LogLevel.ERROR);
    }
    
    @Override
    public boolean isErrorEnabled(Marker marker) {
        return isErrorEnabled();
    }
    
    @Override
    public void error(String msg) {
        log(LogLevel.ERROR, msg, null, null, null, null);
    }
    
    @Override
    public void error(String format, Object arg) {
        log(LogLevel.ERROR, format, arg, null, null, null);
    }
    
    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(LogLevel.ERROR, format, arg1, arg2, null, null);
    }
    
    @Override
    public void error(String format, Object... arguments) {
        log(LogLevel.ERROR, format, null, null, null, arguments);
    }
    
    @Override
    public void error(String msg, Throwable t) {
        log(LogLevel.ERROR, msg, null, t, null, null);
    }
    
    @Override
    public void error(Marker marker, String msg) {
        log(LogLevel.ERROR, msg, marker, null, null, null);
    }
    
    @Override
    public void error(Marker marker, String format, Object arg) {
        log(LogLevel.ERROR, format, arg, marker, null, null);
    }
    
    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        log(LogLevel.ERROR, format, arg1, arg2, marker, null);
    }
    
    @Override
    public void error(Marker marker, String format, Object... arguments) {
        log(LogLevel.ERROR, format, marker, null, null, arguments);
    }
    
    @Override
    public void error(Marker marker, String msg, Throwable t) {
        log(LogLevel.ERROR, msg, marker, t, null, null);
    }
    
    private void log(LogLevel level, String format, Object arg1, Object arg2, 
                     Marker marker, Object[] argsArray) {
        // Format message using SLF4J message formatter
        String formattedMessage;
        if (arg1 != null && arg2 != null) {
            formattedMessage = MessageFormatter.format(format, arg1, arg2).getMessage();
        } else if (arg1 != null) {
            formattedMessage = MessageFormatter.format(format, arg1).getMessage();
        } else if (argsArray != null && argsArray.length > 0) {
            formattedMessage = MessageFormatter.arrayFormat(format, argsArray).getMessage();
        } else {
            formattedMessage = format;
        }
        
        // Add marker as a tag if present
        if (marker != null) {
            chronicleLogger.addTag("marker", marker.getName());
        }
        
        // Log the formatted message
        chronicleLogger.log(level, formattedMessage, (Object[]) null);
    }
    
    /**
     * Factory class for creating Chronicle SLF4J loggers.
     */
    public static class Factory {
        
        private Factory() {
            // Private constructor to prevent instantiation
        }
        
        /**
         * Gets a Chronicle SLF4J logger for the specified class.
         */
        public static Logger getLogger(Class<?> clazz) {
            return new ChronicleSLF4JLogger(clazz.getName());
        }
        
        /**
         * Gets a Chronicle SLF4J logger for the specified name.
         */
        public static Logger getLogger(String name) {
            return new ChronicleSLF4JLogger(name);
        }
    }
}