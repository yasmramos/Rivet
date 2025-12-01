package com.chronicle.logging;

import com.chronicle.logging.api.ChronicleChapter;
import com.chronicle.logging.bridge.ChronicleSLF4JLogger;
import com.chronicle.logging.config.ChronicleConfiguration;
import com.chronicle.logging.config.LogLevel;
import com.chronicle.logging.core.Chronicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Examples demonstrating Chronicle logging capabilities.
 * Shows fluent API, chapter pattern, and SLF4J bridge usage.
 */
public class ChronicleExamples {
    
    public static void main(String[] args) {
        // Configure Chronicle for production use
        configureChronicle();
        
        // Run examples
        exampleFluentAPI();
        exampleChapterPattern();
        exampleSLF4JBridge();
        exampleComplexScenarios();
    }
    
    private static void configureChronicle() {
        ChronicleConfiguration config = ChronicleConfiguration.Builder.create()
            .minLevel(LogLevel.DEBUG)
            .prettyPrint(true)
            .applicationName("ChronicleExamples")
            .applicationVersion("1.0.0")
            .environment("development")
            .build();
        
        // Note: In real usage, you would apply this config to Chronicle
        System.out.println("Chronicle configured for development debugging");
    }
    
    /**
     * Example 1: Fluent API - Clean, type-safe logging with argument interpolation
     */
    private static void exampleFluentAPI() {
        System.out.println("\n=== Example 1: Fluent API ===");
        
        // Basic info logging
        Chronicle.info()
            .message("Application started successfully")
            .log();
        
        // Logging with argument interpolation
        Chronicle.info()
            .message("User {user} logged in from {location}")
            .arg("alice")
            .arg("Madrid, Spain")
            .context("userId", "12345")
            .context("sessionId", "sess-789")
            .tag("security", "auth")
            .tag("level", "info")
            .log();
        
        // Debug logging with multiple arguments
        Chronicle.debug()
            .message("Processing request {requestId} for user {user}")
            .args("req-456", "bob")
            .context("method", "POST")
            .context("endpoint", "/api/users")
            .context("ipAddress", "192.168.1.100")
            .tag("request", "api-call")
            .log();
        
        // Warning with exception context
        Chronicle.warn()
            .message("Rate limit exceeded for user {user}")
            .arg("alice")
            .context("attempts", 5)
            .context("threshold", 3)
            .context("userId", "12345")
            .tag("security", "rate-limit")
            .log();
        
        // Error logging with detailed context
        Chronicle.error()
            .message("Database connection failed")
            .context("host", "db.example.com")
            .context("port", 5432)
            .context("database", "production")
            .context("error", "Connection timeout")
            .tag("database", "connection-error")
            .log();
    }
    
    /**
     * Example 2: Chapter Pattern - Narrative logging with automatic timing
     */
    private static void exampleChapterPattern() {
        System.out.println("\n=== Example 2: Chapter Pattern ===");
        
        // Payment processing chapter
        try (ChronicleChapter chapter = Chronicle.info().beginChapter("Payment Processing")) {
            // Record step-by-step progress
            chapter.record("validation", Map.of(
                "cardValid", true,
                "amount", 99.99,
                "currency", "EUR"
            ));
            
            chapter.step("gateway_check").withData("Payment gateway available");
            
            chapter.record("processing", Map.of(
                "gateway", "Stripe",
                "transactionId", "txn-123456",
                "processorTime", "150ms"
            ));
            
            chapter.context("paymentMethod", "credit_card");
            chapter.context("customerId", "cust-789");
            chapter.tag("payment", "purchase");
            
            // Chapter automatically logs timing and summary when closed
        }
        
        // User registration chapter
        try (ChronicleChapter chapter = Chronicle.debug().beginChapter("User Registration")) {
            chapter.step("validate_input").withData("All required fields present");
            chapter.record("email_check", "Email not in use");
            chapter.record("password_strength", "Strong password");
            chapter.record("profile_creation", "Profile created successfully");
            chapter.record("welcome_email", "Welcome email sent");
            chapter.context("email", "user@example.com");
            chapter.tag("user", "registration");
        }
        
        // Batch processing chapter
        try (ChronicleChapter chapter = Chronicle.warn().beginChapter("Batch Data Processing")) {
            chapter.context("batchId", "batch-20241202-001");
            chapter.context("totalRecords", 10000);
            
            chapter.step("load_data").withData("Loaded 10000 records");
            chapter.step("validate").withData("9500 valid, 500 invalid records");
            chapter.step("transform").withData("Transformations applied");
            
            chapter.record("processing_stats", Map.of(
                "processed", 9500,
                "failed", 500,
                "duration", "2m 30s"
            ));
        }
    }
    
    /**
     * Example 3: SLF4J Bridge - Seamless migration from existing SLF4J code
     */
    private static void exampleSLF4JBridge() {
        System.out.println("\n=== Example 3: SLF4J Bridge ===");
        
        // Create SLF4J logger backed by Chronicle
        Logger slf4jLogger = ChronicleSLF4JLogger.Factory.getLogger(ChronicleExamples.class);
        
        // All standard SLF4J methods work transparently
        slf4jLogger.info("Using Chronicle as SLF4J backend");
        slf4jLogger.debug("Debug message with argument: {}", "test-value");
        slf4jLogger.warn("Warning: Processing time exceeded limit: {} ms", 5000);
        slf4jLogger.error("Error occurred during data processing", new RuntimeException("Test exception"));
        
        // Example from existing codebase - no changes needed!
        Logger existingCodeLogger = ChronicleSLF4JLogger.Factory.getLogger("LegacyModule");
        existingCodeLogger.info("This is existing SLF4J code that now uses Chronicle");
        existingCodeLogger.debug("Legacy debug: Connection pool size: {}", 20);
    }
    
    /**
     * Example 4: Complex scenarios combining multiple features
     */
    private static void exampleComplexScenarios() {
        System.out.println("\n=== Example 4: Complex Scenarios ===");
        
        // Multi-level logging with same context
        Chronicle.debug()
            .message("Starting complex operation")
            .context("operationId", "op-123")
            .context("userId", "user-456")
            .tag("complex", "operation-start")
            .log();
        
        // Later in the same operation
        Chronicle.info()
            .message("Complex operation step 1 completed")
            .context("operationId", "op-123")
            .context("userId", "user-456")
            .context("step", "validation")
            .context("duration", "150ms")
            .tag("complex", "operation-step")
            .log();
        
        // Nested chapters for complex workflows
        try (ChronicleChapter outerChapter = Chronicle.info().beginChapter("Data Synchronization")) {
            outerChapter.context("syncId", "sync-789");
            
            // Inner chapter for database sync
            try (ChronicleChapter dbChapter = Chronicle.debug().beginChapter("Database Sync")) {
                dbChapter.record("connection", "Established");
                dbChapter.record("queries", "Executed 15 queries");
                dbChapter.record("records", "Synchronized 1,250 records");
                dbChapter.tag("sync", "database");
            }
            
            // Inner chapter for external API sync
            try (ChronicleChapter apiChapter = Chronicle.debug().beginChapter("External API Sync")) {
                apiChapter.record("endpoints", 3);
                apiChapter.record("success", 2);
                apiChapter.record("failed", 1);
                apiChapter.tag("sync", "external-api");
            }
            
            outerChapter.record("overall_status", "Completed with minor issues");
        }
        
        // Performance monitoring
        Chronicle.info()
            .message("System performance metrics collected")
            .context("cpu_usage", "45%")
            .context("memory_usage", "2.1GB")
            .context("active_connections", 127)
            .context("requests_per_second", 250)
            .context("avg_response_time", "85ms")
            .tag("performance", "metrics")
            .tag("system", "monitoring")
            .log();
    }
}