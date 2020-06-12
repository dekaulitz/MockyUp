package com.github.dekaulitz.mockyup.filters;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.utils.ConstantsRepository;
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
import java.util.*;

@Component
public class RequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);
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
        if (!StringUtils.isEmpty(ConstantsRepository.REQUEST_ID)) {
            response.addHeader(ConstantsRepository.REQUEST_ID, token);
        }
        MDC.put(ConstantsRepository.REQUEST_ID, token);
        MDC.put(ConstantsRepository.PATH_ENDPOINT, request.getRequestURI());
        request.setAttribute(ConstantsRepository.REQUEST_ID, token);
        request.setAttribute(ConstantsRepository.REQUEST_TIME, start);
        chain.doFilter(request, response);
        final String requestTime = (System.currentTimeMillis() - start) + "ms";
        Map<String, Object> req = new HashMap<>();
        req.put("headers", getRequestHeaders(request));
        req.put("responseTime", requestTime);
        req.put("endpoint", request.getRequestURI());
        req.put("method", request.getMethod());
        req.put("responseStatus", response.getStatus());
        log.info("{}", this.logsMapper.logRequest(req));
        MDC.remove(ConstantsRepository.REQUEST_ID);
        MDC.remove(ConstantsRepository.REQUEST_TIME);
    }

    private String getxRequestID(HttpServletRequest request) {
        final String token;
        if (!StringUtils.isEmpty(ConstantsRepository.REQUEST_ID) && !StringUtils.isEmpty(request.getHeader(ConstantsRepository.REQUEST_ID))) {
            token = request.getHeader(ConstantsRepository.REQUEST_ID);
        } else {
            token = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        }
        return token;
    }


    @Override
    protected boolean isAsyncDispatch(final HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
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

