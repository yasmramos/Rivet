package com.chronicle.logging.api;

import com.chronicle.logging.bridge.ChronicleSLF4JLogger;
import com.chronicle.logging.config.ChronicleConfiguration;
import com.chronicle.logging.config.LogLevel;
import com.chronicle.logging.core.Chronicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ChronicleChapter functionality.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChronicleChapterTest {
    
    private ChronicleLogger logger;
    
    @BeforeEach
    void setUp() {
        ChronicleConfiguration config = new ChronicleConfiguration();
        config.setMinLevel(LogLevel.DEBUG);
        config.setPrettyPrint(false);
        config.setDebugToConsole(false); // Disable console output for tests
        
        logger = new ChronicleLogger("chapter-test", config);
    }
    
    @Test
    void testChapterCreation() {
        assertDoesNotThrow(() -> {
            try (ChronicleChapter chapter = new ChronicleChapter("test-chapter", logger, LogLevel.INFO, 
                                                                null, null, null, null)) {
                assertNotNull(chapter);
                assertEquals("test-chapter", chapter.getChapterName());
            }
        });
    }
    
    @Test
    void testChapterRecording() {
        try (ChronicleChapter chapter = new ChronicleChapter("test-recording", logger, LogLevel.INFO,
                                                            null, null, null, null)) {
            
            // Record some steps
            chapter.record("step1", "value1");
            chapter.record("step2", Map.of("key", "value"));
            
            assertDoesNotThrow(() -> {
                chapter.context("testContext", "testValue");
                chapter.tag("testTag", "tagValue");
            });
        }
    }
    
    @Test
    void testChapterStepRecording() {
        try (ChronicleChapter chapter = new ChronicleChapter("test-step", logger, LogLevel.INFO,
                                                            null, null, null, null)) {
            
            ChronicleChapter.Step step = chapter.step("validation");
            assertNotNull(step);
            
            assertDoesNotThrow(() -> {
                step.withData("validation passed");
                step.withMessage("Step {} completed", "validation");
            });
        }
    }
    
    @Test
    void testChapterMultipleRecords() {
        try (ChronicleChapter chapter = new ChronicleChapter("test-multiple", logger, LogLevel.INFO,
                                                            null, null, null, null)) {
            
            Map<String, Object> records = Map.of(
                "step1", "value1",
                "step2", "value2",
                "step3", Map.of("nested", "data")
            );
            
            assertDoesNotThrow(() -> {
                chapter.records(records);
            });
        }
    }
    
    @Test
    void testChapterContextAndTags() {
        try (ChronicleChapter chapter = new ChronicleChapter("test-context", logger, LogLevel.INFO,
                                                            null, null, null, null)) {
            
            assertDoesNotThrow(() -> {
                chapter.context("key1", "value1");
                chapter.context("key2", 123);
                chapter.tag("tag1", "tagValue1");
                chapter.tag("tag2", "tagValue2");
            });
        }
    }
    
    @Test
    void testChapterTiming() {
        try (ChronicleChapter chapter = new ChronicleChapter("test-timing", logger, LogLevel.INFO,
                                                            null, null, null, null)) {
            
            // Wait a short time to ensure timing works
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            Duration elapsed = chapter.getElapsedTime();
            assertNotNull(elapsed);
            assertTrue(elapsed.toMillis() >= 0);
        }
    }
    
    @Test
    void testChapterClosedState() {
        ChronicleChapter chapter = new ChronicleChapter("test-closed", logger, LogLevel.INFO,
                                                       null, null, null, null);
        
        chapter.close();
        
        // After closing, attempts to record should throw exception
        assertThrows(IllegalStateException.class, () -> {
            chapter.record("step", "value");
        });
        
        assertThrows(IllegalStateException.class, () -> {
            chapter.context("key", "value");
        });
        
        assertThrows(IllegalStateException.class, () -> {
            chapter.tag("tag", "value");
        });
    }
    
    @Test
    void testChapterDoubleClose() {
        ChronicleChapter chapter = new ChronicleChapter("test-double-close", logger, LogLevel.INFO,
                                                       null, null, null, null);
        
        assertDoesNotThrow(() -> {
            chapter.close();
            chapter.close(); // Should not throw on second close
        });
    }
    
    @Test
    void testChapterWithFluentApi() {
        assertDoesNotThrow(() -> {
            ChronicleChapter chapter = Chronicle.info()
                .loggerName("test-fluent")
                .message("Chapter started")
                .arg("test")
                .context("testContext", "testValue")
                .tag("testTag", "tagValue")
                .beginChapter("FluentChapter");
            
            assertNotNull(chapter);
            chapter.close();
        });
    }
}