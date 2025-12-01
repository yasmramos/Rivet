package io.github.yasmramos.rivet.logging.core;

import io.github.yasmramos.rivet.logging.api.RivetLogger;
import io.github.yasmramos.rivet.logging.config.RivetConfiguration;
import io.github.yasmramos.rivet.logging.config.LogLevel;
import io.github.yasmramos.rivet.logging.util.JsonFormatter;
import io.github.yasmramos.rivet.logging.util.ThreadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RivetLogger core functionality.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RivetLoggerTest {
    
    private RivetLogger logger;
    private RivetConfiguration config;
    private JsonFormatter formatter;
    
    @BeforeEach
    void setUp() {
        config = new RivetConfiguration();
        config.setMinLevel(LogLevel.TRACE); // Enable all levels for testing
        config.setPrettyPrint(true);
        config.setDebugToConsole(false); // Disable console output for tests
        
        formatter = new JsonFormatter(config);
        logger = new RivetLogger("test-logger", config);
    }
    
    @Test
    void testLoggerCreation() {
        assertNotNull(logger);
        assertEquals("test-logger", logger.getName());
    }
    
    @Test
    void testLogLevelEnabled() {
        assertTrue(logger.isLevelEnabled(LogLevel.TRACE));
        assertTrue(logger.isLevelEnabled(LogLevel.DEBUG));
        assertTrue(logger.isLevelEnabled(LogLevel.INFO));
        assertTrue(logger.isLevelEnabled(LogLevel.WARN));
        assertTrue(logger.isLevelEnabled(LogLevel.ERROR));
        
        config.setMinLevel(LogLevel.WARN);
        assertFalse(logger.isLevelEnabled(LogLevel.TRACE));
        assertFalse(logger.isLevelEnabled(LogLevel.DEBUG));
        assertFalse(logger.isLevelEnabled(LogLevel.INFO));
        assertTrue(logger.isLevelEnabled(LogLevel.WARN));
        assertTrue(logger.isLevelEnabled(LogLevel.ERROR));
    }
    
    @Test
    void testContextOperations() {
        logger.addContext("testKey", "testValue");
        assertEquals("testValue", ThreadContext.get().get("testKey"));
        
        logger.removeContext("testKey");
        assertNull(ThreadContext.get().get("testKey"));
    }
    
    @Test
    void testTagOperations() {
        logger.addTag("testTag", "testValue");
        assertEquals("testValue", ThreadContext.get().getTag("testTag"));
        
        logger.removeTag("testTag");
        assertNull(ThreadContext.get().getTag("testTag"));
    }
    
    @Test
    void testSimpleLogging() {
        // Test basic logging doesn't throw exceptions
        assertDoesNotThrow(() -> {
            logger.log(LogLevel.INFO, "Test message", new HashMap<>(), new HashMap<>(), new Object[0]);
        });
    }
    
    @Test
    void testLoggingWithArguments() {
        assertDoesNotThrow(() -> {
            logger.log(LogLevel.INFO, "User {0} logged in", new HashMap<>(), new HashMap<>(), 
                      new Object[]{"alice"});
        });
    }
    
    @Test
    void testLoggingWithContextAndTags() {
        Map<String, Object> context = new HashMap<>();
        context.put("userId", "12345");
        
        Map<String, String> tags = new HashMap<>();
        tags.put("security", "auth");
        
        assertDoesNotThrow(() -> {
            logger.log(LogLevel.INFO, "User logged in", context, tags, new Object[0]);
        });
    }
    
    @Test
    void testLogLevelOrdering() {
        assertTrue(LogLevel.TRACE.ordinal() < LogLevel.DEBUG.ordinal());
        assertTrue(LogLevel.DEBUG.ordinal() < LogLevel.INFO.ordinal());
        assertTrue(LogLevel.INFO.ordinal() < LogLevel.WARN.ordinal());
        assertTrue(LogLevel.WARN.ordinal() < LogLevel.ERROR.ordinal());
    }
    
    @Test
    void testLevelComparisonMethods() {
        LogLevel info = LogLevel.INFO;
        assertTrue(info.isEnabled(LogLevel.TRACE));
        assertTrue(info.isEnabled(LogLevel.DEBUG));
        assertTrue(info.isEnabled(LogLevel.INFO));
        assertFalse(info.isEnabled(LogLevel.WARN));
        assertFalse(info.isEnabled(LogLevel.ERROR));
        
        assertTrue(info.isAtLeast(LogLevel.TRACE));
        assertTrue(info.isAtLeast(LogLevel.DEBUG));
        assertTrue(info.isAtLeast(LogLevel.INFO));
        assertFalse(info.isAtLeast(LogLevel.WARN));
        
        assertFalse(info.isAtMost(LogLevel.TRACE));
        assertFalse(info.isAtMost(LogLevel.DEBUG));
        assertTrue(info.isAtMost(LogLevel.INFO));
        assertTrue(info.isAtMost(LogLevel.WARN));
        assertTrue(info.isAtMost(LogLevel.ERROR));
    }
}