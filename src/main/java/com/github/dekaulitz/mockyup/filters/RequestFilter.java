package com.github.dekaulitz.mockyup.filters;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

@Component
@Data
public class RequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);
    private final static String responseId = "responseId";
    private final static String clientIp = "clientIp";


    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws java.io.IOException, ServletException {
        long start = System.currentTimeMillis();
        try {
            final String token = getxRequestID(request);
            MDC.put(clientIp, getClientIP(request));
            MDC.put(responseId, getLocalHostName() + "-" + token);
            if (!StringUtils.isEmpty(responseId)) {
                response.addHeader(responseId, getLocalHostName() + "-" + token);
            }
            chain.doFilter(request, response);
            log.info("{}", LogsMapper.logRequest(getRequestHeaders(request)));
        } finally {
            MDC.remove(clientIp);
            MDC.remove(responseId);
        }
    }

    private String getxRequestID(final HttpServletRequest request) {
        final String token;
        if (!StringUtils.isEmpty(responseId) && !StringUtils.isEmpty(request.getHeader(responseId))) {
            token = request.getHeader(responseId);
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

