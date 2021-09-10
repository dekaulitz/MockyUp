package com.github.dekaulitz.mockyup.server.configuration.filter;

import com.github.dekaulitz.mockyup.server.model.request.IssuerRequestModel;
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

  private final String LOCALHOST_IPV4 = "127.0.0.1";
  private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    String requestId = UUID.randomUUID().toString();
    long requestTime = System.currentTimeMillis();
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    IssuerRequestModel issuerRequestModel = IssuerRequestModel.builder()
        .agent(req.getHeader("User-Agent"))
        .ip(getClientIp(req))
        .requestId(requestId)
        .build();
    req.setAttribute("x-request-id", issuerRequestModel.getRequestId());
    req.setAttribute("x-request-time", requestTime);
    servletRequest.getServletContext().setAttribute("issuer", issuerRequestModel);
    log.info(
        "Starting a transaction for req : {}",
        req.getRequestURI());
    HttpServletResponse res = (HttpServletResponse) servletResponse;
    res.setHeader("x-request-id", issuerRequestModel.getRequestId());
    res.setHeader("x-request-time", String.valueOf(requestTime));
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

