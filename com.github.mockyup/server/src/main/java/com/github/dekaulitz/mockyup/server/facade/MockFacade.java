package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.service.mockup.api.MockingService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockFacade {

  @Autowired
  private ProjectContractService projectContractService;
  @Autowired
  private MockingService mockingService;

}
