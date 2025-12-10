package com.vfsglobal.utilities.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";
    private static final String TRACE_ID_MDC_KEY = "traceId";
    private static final String SPAN_ID_MDC_KEY = "spanId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Get or generate correlation ID
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.isEmpty()) {
                correlationId = UUID.randomUUID().toString();
            }

            // Add correlation ID to response header
            response.setHeader(CORRELATION_ID_HEADER, correlationId);

            // Add to MDC for logging
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
            
            // Try to get trace ID and span ID from OpenTelemetry context if available
            // For now, we'll use correlation ID as trace ID if not available
            String traceId = getTraceId();
            String spanId = getSpanId();
            
            if (traceId != null) {
                MDC.put(TRACE_ID_MDC_KEY, traceId);
            } else {
                MDC.put(TRACE_ID_MDC_KEY, correlationId);
            }
            
            if (spanId != null) {
                MDC.put(SPAN_ID_MDC_KEY, spanId);
            }

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String getTraceId() {
        // In a real implementation, this would extract trace ID from OpenTelemetry context
        // For now, return null to use correlation ID
        return null;
    }

    private String getSpanId() {
        // In a real implementation, this would extract span ID from OpenTelemetry context
        // For now, return null
        return null;
    }
}

