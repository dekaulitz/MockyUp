package com.github.dekaulitz.mockyup.filters;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@Data
public class RequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);
    private final String REQUEST_ID = "requestId";
    private final String CLIENT_ID = "clientIp";
    private final String REQUEST_TIME = "requestTime";
    @Autowired
    private final LogsMapper logsMapper;

    public RequestFilter(LogsMapper logsMapper) {
        this.logsMapper = logsMapper;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws java.io.IOException, ServletException {
        long start = System.currentTimeMillis();
        final String token = getxRequestID(request);
        MDC.put(CLIENT_ID, getClientIP(request));
        MDC.put(REQUEST_ID, getLocalHostName() + "-" + token);
        if (!StringUtils.isEmpty(REQUEST_ID)) {
            response.addHeader(REQUEST_ID, getLocalHostName() + "-" + token);
        }
        chain.doFilter(request, response);
        MDC.put(REQUEST_TIME, (System.currentTimeMillis() - start) + "ms");
        Map<String, Object> req = new HashMap<>();
        req.put("headers", getRequestHeaders(request));
        req.put("endpoint", request.getRequestURI());
        req.put("method", request.getMethod());
        req.put("responseStatus", response.getStatus());
        CompletableFuture.runAsync(() -> {
            log.info("{}", this.logsMapper.logRequest(req));
            MDC.remove(CLIENT_ID);
            MDC.remove(REQUEST_ID);
            MDC.remove(REQUEST_TIME);
        });
    }

    private String getxRequestID(final HttpServletRequest request) {
        final String token;
        if (!StringUtils.isEmpty(REQUEST_ID) && !StringUtils.isEmpty(request.getHeader(REQUEST_ID))) {
            token = request.getHeader(REQUEST_ID);
        } else {
            token = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        }
        return token;
    }

    private String getClientIP(final HttpServletRequest request) {
        final String clientIP;
        if (request.getHeader("X-Forwarded-For") != null) {
            clientIP = request.getHeader("X-Forwarded-For").split(",")[0];
        } else {
            clientIP = request.getRemoteAddr();
        }
        return clientIP;
    }

    @Override
    protected boolean isAsyncDispatch(final HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    private String getLocalHostName() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;

    }

    private Map<String, Object> getResponsetHeaders(ContentCachingResponseWrapper response) {
        Map<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }

}

