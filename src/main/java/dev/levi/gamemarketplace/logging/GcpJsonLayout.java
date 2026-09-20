package dev.levi.gamemarketplace.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.LayoutBase;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class GcpJsonLayout extends LayoutBase<ILoggingEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String doLayout(ILoggingEvent event) {
        Map<String, Object> logEntry = new LinkedHashMap<>();

        logEntry.put("severity", mapSeverity(event.getLevel().toString()));
        logEntry.put("message", event.getFormattedMessage());
        logEntry.put("timestamp", Instant.ofEpochMilli(event.getTimeStamp()).toString());
        logEntry.put("logger", event.getLoggerName());
        logEntry.put("thread", event.getThreadName());

        if (!event.getMDCPropertyMap().isEmpty()) {
            logEntry.putAll(event.getMDCPropertyMap());
        }

        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            logEntry.put("exception", ThrowableProxyUtil.asString(throwableProxy));
        }

        try {
            return objectMapper.writeValueAsString(logEntry) + System.lineSeparator();
        } catch (Exception e) {
            return "{\"severity\":\"ERROR\",\"message\":\"Failed to serialize log entry\"}" + System.lineSeparator();
        }
    }

    private String mapSeverity(String level) {
        return switch (level) {
            case "WARN" -> "WARNING";
            case "ERROR" -> "ERROR";
            case "DEBUG", "TRACE" -> "DEBUG";
            default -> "INFO";
        };
    }
}
