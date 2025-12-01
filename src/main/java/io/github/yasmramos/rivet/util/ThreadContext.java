package io.github.yasmramos.rivet.logging.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-local context for storing logging context and tags.
 * Each thread maintains its own context that gets included in log entries.
 */
public class ThreadContext {
    
    private static final ThreadLocal<ThreadContext> THREAD_LOCAL = 
        ThreadLocal.withInitial(ThreadContext::new);
    
    private final Map<String, Object> context = new ConcurrentHashMap<>();
    private final Map<String, String> tags = new ConcurrentHashMap<>();
    
    /**
     * Gets the thread context for the current thread.
     */
    public static ThreadContext get() {
        return THREAD_LOCAL.get();
    }
    
    /**
     * Puts a context value.
     */
    public void put(String key, Object value) {
        if (key != null) {
            context.put(key, value);
        }
    }
    
    /**
     * Gets a context value.
     */
    public Object get(String key) {
        return context.get(key);
    }
    
    /**
     * Removes a context value.
     */
    public void remove(String key) {
        context.remove(key);
    }
    
    /**
     * Puts a tag.
     */
    public void putTag(String tag, String value) {
        if (tag != null) {
            tags.put(tag, value);
        }
    }
    
    /**
     * Gets a tag value.
     */
    public String getTag(String tag) {
        return tags.get(tag);
    }
    
    /**
     * Removes a tag.
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }
    
    /**
     * Gets all context values.
     */
    public Map<String, Object> getAll() {
        return new ConcurrentHashMap<>(context);
    }
    
    /**
     * Gets all tags.
     */
    public Map<String, String> getTags() {
        return new ConcurrentHashMap<>(tags);
    }
    
    /**
     * Clears all context and tags.
     */
    public void clear() {
        context.clear();
        tags.clear();
    }
    
    /**
     * Clears all context values.
     */
    public void clearContext() {
        context.clear();
    }
    
    /**
     * Clears all tags.
     */
    public void clearTags() {
        tags.clear();
    }
    
    /**
     * Checks if context is empty.
     */
    public boolean isContextEmpty() {
        return context.isEmpty();
    }
    
    /**
     * Checks if tags is empty.
     */
    public boolean isTagsEmpty() {
        return tags.isEmpty();
    }
    
    /**
     * Gets the number of context entries.
     */
    public int contextSize() {
        return context.size();
    }
    
    /**
     * Gets the number of tags.
     */
    public int tagsSize() {
        return tags.size();
    }
    
    /**
     * Clears the thread-local context for the current thread.
     */
    public static void clearAll() {
        THREAD_LOCAL.remove();
    }
}