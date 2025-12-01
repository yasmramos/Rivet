package io.github.yasmramos.rivet.logging.api;

import io.github.yasmramos.rivet.logging.config.LogLevel;
import io.github.yasmramos.rivet.logging.util.TimestampProvider;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chapter-based logging that creates narrative log entries with automatic timing.
 * Implements try-with-resources pattern for automatic logging on close.
 * 
 * Example usage:
 * try (RivetChapter chapter = Rivet.beginChapter("Payment Processing")) {
 *     chapter.record("validation", validationDetails);
 *     chapter.record("processing", paymentDetails);
 *     // Auto-log al final con timing y resumen
 * }
 */
public class RivetChapter implements AutoCloseable {
    
    private final String chapterName;
    private final RivetLogger logger;
    private final LogLevel level;
    private final String startMessage;
    private final Object[] startArgs;
    private final Map<String, Object> context;
    private final Map<String, String> tags;
    
    private final Instant startTime;
    private final Map<String, Object> records;
    private boolean closed = false;
    
    public RivetChapter(String chapterName, RivetLogger logger, LogLevel level, 
                        String startMessage, Object[] startArgs, 
                        Map<String, Object> context, Map<String, String> tags) {
        this.chapterName = chapterName;
        this.logger = logger;
        this.level = level;
        this.startMessage = startMessage;
        this.startArgs = startArgs != null ? Arrays.copyOf(startArgs, startArgs.length) : new Object[0];
        this.context = new HashMap<>(context != null ? context : new HashMap<>());
        this.tags = new HashMap<>(tags != null ? tags : new HashMap<>());
        this.startTime = Instant.now();
        this.records = new ConcurrentHashMap<>();
        
        // Log chapter start
        logChapterStart();
    }
    
    /**
     * Records a step in the chapter with name and data.
     */
    public RivetChapter record(String stepName, Object stepData) {
        if (closed) {
            throw new IllegalStateException("Cannot record to closed chapter: " + chapterName);
        }
        
        records.put(stepName, stepData);
        return this;
    }
    
    /**
     * Records multiple steps at once.
     */
    public RivetChapter records(Map<String, Object> steps) {
        if (closed) {
            throw new IllegalStateException("Cannot record to closed chapter: " + chapterName);
        }
        
        this.records.putAll(steps);
        return this;
    }
    
    /**
     * Adds context data to the chapter.
     */
    public RivetChapter context(String key, Object value) {
        if (closed) {
            throw new IllegalStateException("Cannot modify closed chapter: " + chapterName);
        }
        
        context.put(key, value);
        return this;
    }
    
    /**
     * Adds a tag to the chapter.
     */
    public RivetChapter tag(String tag, String value) {
        if (closed) {
            throw new IllegalStateException("Cannot modify closed chapter: " + chapterName);
        }
        
        tags.put(tag, value);
        return this;
    }
    
    /**
     * Gets the elapsed time since chapter started.
     */
    public Duration getElapsedTime() {
        return Duration.between(startTime, Instant.now());
    }
    
    /**
     * Gets the chapter name.
     */
    public String getChapterName() {
        return chapterName;
    }
    
    /**
     * Closes the chapter and logs the final narrative entry.
     */
    @Override
    public void close() {
        if (closed) {
            return; // Already closed
        }
        
        closed = true;
        logChapterEnd();
    }
    
    private void logChapterStart() {
        Map<String, Object> startContext = new HashMap<>(context);
        startContext.put("chapter.name", chapterName);
        startContext.put("chapter.phase", "START");
        startContext.put("chapter.startTime", startTime.toString());
        startContext.put("chapter.duration_ms", 0);
        
        // Add chapter start message if provided
        if (startMessage != null) {
            startContext.put("message", startMessage);
        }
        
        logger.log(level, "Chapter started: " + chapterName, startContext, tags, startArgs);
    }
    
    private void logChapterEnd() {
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        
        Map<String, Object> endContext = new HashMap<>(context);
        endContext.put("chapter.name", chapterName);
        endContext.put("chapter.phase", "END");
        endContext.put("chapter.startTime", startTime.toString());
        endContext.put("chapter.endTime", endTime.toString());
        endContext.put("chapter.duration_ms", duration.toMillis());
        endContext.put("chapter.duration_ns", duration.toNanos());
        
        // Add all recorded steps
        for (Map.Entry<String, Object> record : records.entrySet()) {
            endContext.put("chapter.step." + record.getKey(), record.getValue());
        }
        
        // Add step count and summary
        endContext.put("chapter.steps.count", records.size());
        endContext.put("chapter.steps", records.keySet());
        
        // Add success status
        endContext.put("chapter.status", "SUCCESS");
        
        logger.log(level, "Chapter completed: " + chapterName + " (took " + duration.toMillis() + "ms)", 
                  endContext, tags, startArgs);
    }
    
    /**
     * Inner class for step-by-step recording with method chaining.
     */
    public static class Step {
        private final RivetChapter chapter;
        private final String stepName;
        
        public Step(RivetChapter chapter, String stepName) {
            this.chapter = chapter;
            this.stepName = stepName;
        }
        
        public RivetChapter withData(Object data) {
            return chapter.record(stepName, data);
        }
        
        public RivetChapter withMessage(String message, Object... args) {
            return chapter.record(stepName, 
                message != null ? String.format(message, args) : message);
        }
    }
    
    /**
     * Creates a new step for recording.
     */
    public Step step(String stepName) {
        return new Step(this, stepName);
    }
}