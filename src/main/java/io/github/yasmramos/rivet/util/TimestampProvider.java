package io.github.yasmramos.rivet.logging.util;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Provides timestamp functionality for Chronicle logging.
 * Handles timezone-aware timestamp generation and formatting.
 */
public class TimestampProvider {
    
    private final Clock clock;
    private final ZoneId timezone;
    
    public TimestampProvider(ZoneId timezone) {
        this.clock = Clock.systemDefaultZone();
        this.timezone = timezone != null ? timezone : ZoneId.systemDefault();
    }
    
    public TimestampProvider(Clock clock, ZoneId timezone) {
        this.clock = clock;
        this.timezone = timezone != null ? timezone : ZoneId.systemDefault();
    }
    
    /**
     * Gets the current timestamp in the configured timezone.
     */
    public Instant now() {
        return Instant.now(clock);
    }
    
    /**
     * Gets the current timestamp as a ZonedDateTime.
     */
    public ZonedDateTime nowZoned() {
        return ZonedDateTime.now(timezone);
    }
    
    /**
     * Gets the configured timezone.
     */
    public ZoneId getTimezone() {
        return timezone;
    }
    
    /**
     * Creates a TimestampProvider with system default timezone.
     */
    public static TimestampProvider systemDefault() {
        return new TimestampProvider(ZoneId.systemDefault());
    }
    
    /**
     * Creates a TimestampProvider with UTC timezone.
     */
    public static TimestampProvider utc() {
        return new TimestampProvider(ZoneId.of("UTC"));
    }
    
    /**
     * Creates a TimestampProvider with a specific timezone.
     */
    public static TimestampProvider of(String timezone) {
        return new TimestampProvider(ZoneId.of(timezone));
    }
    
    /**
     * Creates a fixed clock timestamp provider for testing.
     */
    public static TimestampProvider fixed(Instant instant) {
        return new TimestampProvider(Clock.fixed(instant, ZoneId.systemDefault()), ZoneId.systemDefault());
    }
}