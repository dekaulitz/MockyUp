package com.github.dekaulitz.mockyup.server.service.mockup.api;

import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface MockingService {

  Object mockingRequest(String id, String path, String body, String httpMethod,
      Map<String, String> headers, Map<String, String[]> parameters) throws ServiceException;
}
