package com.github.dekaulitz.mockyup.server.controllers.filter;

import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.constants.Language;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RequestFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    String requestId = UUID.randomUUID().toString();
    long requestTime = System.currentTimeMillis();
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    MandatoryModel mandatoryModel = MandatoryModel.builder()
        .agent(req.getHeader("User-Agent"))
        .ip(getClientIp(req))
        .requestId(requestId)
        .requestTime(requestTime)
        .language(Language.EN)
        .build();
    req.setAttribute(ApplicationConstants.X_REQUEST_ID, mandatoryModel.getRequestId());
    req.setAttribute(ApplicationConstants.X_REQUEST_TIME, mandatoryModel.getRequestTime());
    servletRequest.getServletContext()
        .setAttribute(ApplicationConstants.MANDATORY, mandatoryModel);
    log.info(
        "Starting a transaction for req : {}",
        req.getRequestURI());
    HttpServletResponse res = (HttpServletResponse) servletResponse;
    res.setHeader(ApplicationConstants.X_REQUEST_ID, mandatoryModel.getRequestId());
    res.setHeader(ApplicationConstants.X_REQUEST_TIME, String.valueOf(mandatoryModel.getRequestTime()));
    filterChain.doFilter(servletRequest, servletResponse);
    log.info(
        "Committing a transaction for req : {}",
        req.getRequestURI());
  }

  public String getClientIp(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }

    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }

    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
      String LOCALHOST_IPV4 = "127.0.0.1";
      String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
      if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
        try {
          InetAddress inetAddress = InetAddress.getLocalHost();
          ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
      }
    }

    if (!StringUtils.isEmpty(ipAddress)
        && ipAddress.length() > 15
        && ipAddress.indexOf(",") > 0) {
      ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
    }

    return ipAddress;
  }
}

