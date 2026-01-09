package io.github.yasmramos.rivet.logging;

import io.github.yasmramos.rivet.logging.bridge.RivetSLF4JLogger;
import io.github.yasmramos.rivet.logging.config.RivetConfiguration;
import io.github.yasmramos.rivet.logging.config.LogLevel;
import io.github.yasmramos.rivet.logging.core.Rivet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Examples demonstrating Rivet logging capabilities.
 * Shows fluent API and SLF4J bridge usage.
 */
public class RivetExamples {
    
    public static void main(String[] args) {
        // Configure Rivet for production use
        configureRivet();
        
        // Run examples
        exampleFluentAPI();
        exampleSLF4JBridge();
        exampleComplexScenarios();
    }
    
    private static void configureRivet() {
        RivetConfiguration config = RivetConfiguration.Builder.create()
            .minLevel(LogLevel.DEBUG)
            .prettyPrint(true)
            .applicationName("RivetExamples")
            .applicationVersion("1.0.0")
            .environment("development")
            .build();
        
        // Note: In real usage, you would apply this config to Rivet
        System.out.println("Rivet configured for development debugging");
    }
    
    /**
     * Example 1: Fluent API - Clean, type-safe logging with argument interpolation
     */
    private static void exampleFluentAPI() {
        System.out.println("\n=== Example 1: Fluent API ===");
        
        // Basic info logging
        Rivet.info()
            .message("Application started successfully")
            .log();
        
        // Logging with argument interpolation
        Rivet.info()
            .message("User {user} logged in from {location}")
            .arg("alice")
            .arg("Madrid, Spain")
            .context("userId", "12345")
            .context("sessionId", "sess-789")
            .tag("security", "auth")
            .tag("level", "info")
            .log();
        
        // Debug logging with multiple arguments
        Rivet.debug()
            .message("Processing request {requestId} for user {user}")
            .args("req-456", "bob")
            .context("method", "POST")
            .context("endpoint", "/api/users")
            .context("ipAddress", "192.168.1.100")
            .tag("request", "api-call")
            .log();
        
        // Warning with exception context
        Rivet.warn()
            .message("Rate limit exceeded for user {user}")
            .arg("alice")
            .context("attempts", 5)
            .context("threshold", 3)
            .context("userId", "12345")
            .tag("security", "rate-limit")
            .log();
        
        // Error logging with detailed context
        Rivet.error()
            .message("Database connection failed")
            .context("host", "db.example.com")
            .context("port", 5432)
            .context("database", "production")
            .context("error", "Connection timeout")
            .tag("database", "connection-error")
            .log();
    }
    
    /**
     * Example 2: SLF4J Bridge - Seamless migration from existing SLF4J code
     */
    private static void exampleSLF4JBridge() {
        System.out.println("\n=== Example 2: SLF4J Bridge ===");
        
        // Create SLF4J logger backed by Rivet
        Logger slf4jLogger = RivetSLF4JLogger.Factory.getLogger(RivetExamples.class);
        
        // All standard SLF4J methods work transparently
        slf4jLogger.info("Using Rivet as SLF4J backend");
        slf4jLogger.debug("Debug message with argument: {}", "test-value");
        slf4jLogger.warn("Warning: Processing time exceeded limit: {} ms", 5000);
        slf4jLogger.error("Error occurred during data processing", new RuntimeException("Test exception"));
        
        // Example from existing codebase - no changes needed!
        Logger existingCodeLogger = RivetSLF4JLogger.Factory.getLogger("LegacyModule");
        existingCodeLogger.info("This is existing SLF4J code that now uses Rivet");
        existingCodeLogger.debug("Legacy debug: Connection pool size: {}", 20);
    }
    
    /**
     * Example 3: Complex scenarios combining multiple features
     */
    private static void exampleComplexScenarios() {
        System.out.println("\n=== Example 3: Complex Scenarios ===");
        
        // Multi-level logging with same context
        Rivet.debug()
            .message("Starting complex operation")
            .context("operationId", "op-123")
            .context("userId", "user-456")
            .tag("complex", "operation-start")
            .log();
        
        // Later in the same operation
        Rivet.info()
            .message("Complex operation step 1 completed")
            .context("operationId", "op-123")
            .context("userId", "user-456")
            .context("step", "validation")
            .context("duration", "150ms")
            .tag("complex", "operation-step")
            .log();
        
        // Performance monitoring
        Rivet.info()
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
