package com.chronicle.logging.util;

import com.chronicle.logging.config.RivetConfiguration;
import com.chronicle.logging.core.LogEntry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Formats LogEntry objects into JSON strings.
 * Provides customizable JSON output with timestamps, context, and tags.
 */
public class JsonFormatter {
    
    private final RivetConfiguration configuration;
    private final DateTimeFormatter timestampFormatter;
    
    public JsonFormatter(RivetConfiguration configuration) {
        this.configuration = configuration;
        this.timestampFormatter = DateTimeFormatter.ISO_INSTANT;
    }
    
    /**
     * Formats a LogEntry into a JSON string.
     */
    public String format(LogEntry entry) {
        JSONObject json = new JSONObject();
        
        // Add timestamp
        json.put("@timestamp", timestampFormatter.format(entry.getTimestamp()));
        
        // Add log level and logger information
        json.put("level", entry.getLevel().name().toLowerCase());
        json.put("logger", entry.getLoggerName());
        json.put("message", entry.getMessage());
        
        // Add thread information
        JSONObject threadInfo = new JSONObject();
        threadInfo.put("id", entry.getThreadId());
        threadInfo.put("name", entry.getThreadName());
        json.put("thread", threadInfo);
        
        // Add context if present
        if (!entry.getContext().isEmpty()) {
            json.put("context", new JSONObject(entry.getContext()));
        }
        
        // Add tags if present
        if (!entry.getTags().isEmpty()) {
            json.put("tags", new JSONObject(entry.getTags()));
        }
        
        // Add custom fields from configuration
        addCustomFields(json, entry);
        
        // Add metadata
        json.put("chronicle.version", "1.0.0");
        json.put("chronicle.formatter", "json");
        
        return json.toString(configuration.isPrettyPrint() ? 2 : 0);
    }
    
    /**
     * Formats multiple LogEntries into a JSON array.
     */
    public String formatBatch(Iterable<LogEntry> entries) {
        JSONArray jsonArray = new JSONArray();
        
        for (LogEntry entry : entries) {
            JSONObject json = formatToJson(entry);
            jsonArray.put(json);
        }
        
        return jsonArray.toString(configuration.isPrettyPrint() ? 2 : 0);
    }
    
    /**
     * Formats a LogEntry into a JSONObject.
     */
    public JSONObject formatToJson(LogEntry entry) {
        JSONObject json = new JSONObject();
        
        // Add timestamp
        json.put("@timestamp", timestampFormatter.format(entry.getTimestamp()));
        
        // Add log level and logger information
        json.put("level", entry.getLevel().name().toLowerCase());
        json.put("logger", entry.getLoggerName());
        json.put("message", entry.getMessage());
        
        // Add thread information
        JSONObject threadInfo = new JSONObject();
        threadInfo.put("id", entry.getThreadId());
        threadInfo.put("name", entry.getThreadName());
        json.put("thread", threadInfo);
        
        // Add context if present
        if (!entry.getContext().isEmpty()) {
            json.put("context", new JSONObject(entry.getContext()));
        }
        
        // Add tags if present
        if (!entry.getTags().isEmpty()) {
            json.put("tags", new JSONObject(entry.getTags()));
        }
        
        // Add metadata
        json.put("chronicle.version", "1.0.0");
        json.put("chronicle.formatter", "json");
        
        return json;
    }
    
    private void addCustomFields(JSONObject json, LogEntry entry) {
        // Add hostname if configured
        if (configuration.isIncludeHostname()) {
            json.put("hostname", getHostname());
        }
        
        // Add application name if configured
        if (configuration.getApplicationName() != null) {
            json.put("application", configuration.getApplicationName());
        }
        
        // Add environment if configured
        if (configuration.getEnvironment() != null) {
            json.put("environment", configuration.getEnvironment());
        }
        
        // Add version if configured
        if (configuration.getApplicationVersion() != null) {
            json.put("version", configuration.getApplicationVersion());
        }
    }
    
    private String getHostname() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }
    
    /**
     * Adds a custom field to the formatter configuration.
     */
    public void addCustomField(String key, Object value) {
        // Custom fields would be stored in configuration
        // This is a placeholder for future functionality
    }
}