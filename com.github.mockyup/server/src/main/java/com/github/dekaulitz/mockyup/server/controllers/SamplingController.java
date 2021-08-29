package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.model.param.GetMockUpParam;
import com.github.dekaulitz.mockyup.server.model.response.MockupResponse;
import com.github.dekaulitz.mockyup.server.service.mockup.api.MockupService;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SamplingController {

  @Autowired
  private MockupService mockupService;
  @Autowired
  private ModelMapper modelMapper;

  @RequestMapping(value = "/sampling", method = {RequestMethod.GET}
  )
  public ResponseEntity<Object> mockingPath(@Valid GetMockUpParam getMockUpParam) {
    List<MockupResponse> mockEntitiesList = mockupService.getAll(getMockUpParam);
    return ResponseEntity.ok(mockEntitiesList);
  }

  @RequestMapping(value = "/sampling/{id}", method = {RequestMethod.GET}
  )
  public ResponseEntity<Object> mockingPath(@PathVariable String id) {
    MockEntities mockEntitiesList = mockupService.getById(id);
    return ResponseEntity.ok(mockEntitiesList);
  }
}
