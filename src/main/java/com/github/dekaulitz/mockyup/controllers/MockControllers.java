package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.base.controller.BaseController;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.base.MockInterface;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.CreatorMockVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * mocks handling
 */
@RestController
public class MockControllers extends BaseController {

  @Autowired
  private final MongoTemplate mongoTemplate;

  @Autowired
  private final MockInterface mockInterface;


  public MockControllers(MongoTemplate mongoTemplate, MockInterface mockInterface) {
    this.mongoTemplate = mongoTemplate;
    this.mockInterface = mockInterface;
  }

  /**
   * get the mockyup swagger contract
   *
   * @return ResponseEntity
   * @throws IOException IOException
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @GetMapping(value = "/mocks/docs", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> greeting() throws IOException {
    Resource resource = new ClassPathResource("/public/swagger.json");
    InputStreamReader isReader = new InputStreamReader(resource.getInputStream());
    BufferedReader reader = new BufferedReader(isReader);
    StringBuffer sb = new StringBuffer();
    String str;
    while ((str = reader.readLine()) != null) {
      sb.append(str);
    }
    return ResponseEntity.ok(sb.toString());
  }

  /**
   * @param path path on mock swagger pathitem that want to get mocking the response
   * @param id   mock id from  mock collection
   * @param body optional depend on the contract that registered
   * @return ResponseEntity
   */
  @RequestMapping(value = "/mocks/mocking/{id}", method = {RequestMethod.OPTIONS,
      RequestMethod.DELETE,
      RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH,
      RequestMethod.PUT,
      RequestMethod.TRACE}
  )
  public ResponseEntity<Object> mockingPath(@NonNull @RequestParam(value = "path") String path,
      @PathVariable String id, @RequestBody(required = false) String body,
      HttpServletRequest request) {
    MockHelper mock;
    try {
      mock = this.mockInterface.getMockMocking(request, path, id, body);
      if (mock != null) {
        return this.generateMockResponseEntity(mock);
      }
      return new ResponseEntity<>("no example mock found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * getting all user on the mock
   *
   * @param id mockid from mock collection
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @GetMapping(value = "/mocks/{id}/users",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getUserMocks(@PathVariable String id) {
    return ResponseEntity.ok(this.mockInterface.getUsersListOfMocks(id));
  }

  /**
   * get access permission base on token for checking has access to modified
   *
   * @param id      mockid from mock collection
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @GetMapping(value = "/mocks/{id}/detailWithAccess",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getCurrentAccess(@PathVariable String id,
      HttpServletRequest request) {
    try {
      List<DtoMockupDetailVmodel> mockDetailWithAccess = this.mockInterface
          .getDetailMockUpIdByUserAccess(id,
              this.getAuthenticationProfileModel());
      if (mockDetailWithAccess.isEmpty()) {
        throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
      }
      return ResponseEntity.ok(mockDetailWithAccess.get(0));
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * getting all histories on the mock
   *
   * @param id id from mock collection
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @GetMapping(value = "/mocks/{id}/histories",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getMockHistories(@PathVariable String id) {
    return ResponseEntity.ok(this.mockInterface.getMockHistories(id));
  }

  /**
   * add user access into mock
   *
   * @param id      mockid from mock  collection
   * @param vmodel  user data
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @PutMapping(value = "/mocks/{id}/addUser",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> addUserAccess(@PathVariable String id,
      @Valid @RequestBody AddUserAccessVmodel vmodel,
      HttpServletRequest request) {
    try {
      return ResponseEntity
          .ok(this.mockInterface.addUserToMock(id, vmodel, this.getAuthenticationProfileModel()));
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * removing access user on mock
   *
   * @param id      id from mock collection
   * @param userId  id id from user collection
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @DeleteMapping(value = "/mocks/{id}/remove/{userId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> removingAccessUser(@PathVariable String id,
      @PathVariable String userId, HttpServletRequest request) {
    try {
      return ResponseEntity.ok(this.mockInterface
          .removeAccessUserOnMock(id, userId, this.getAuthenticationProfileModel()));
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * get swagger mock history
   *
   * @param id        id from mock collection
   * @param historyId id from mock history collection
   * @param request   HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @GetMapping(value = "/mocks/{id}/histories/{historyId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getSpecHistory(@PathVariable String id,
      @PathVariable String historyId, HttpServletRequest request) {
    try {
      return ResponseEntity.ok(this.mockInterface
          .geMockHistoryId(id, historyId, this.getAuthenticationProfileModel()));
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * @param body mock payload data
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
  @PostMapping(value = "/mocks/store")
  public ResponseEntity<Object> storeMocksEntity(@Valid @RequestBody MockVmodel body,
      HttpServletRequest request) {
    try {
      MockEntities mock = this.mockInterface.save(body, this.getAuthenticationProfileModel());
      Paths newPath = new Paths();
      body.setId(mock.getId());
      OpenAPI openAPI = Json.mapper().readValue(mock.getSpec(), OpenAPI.class);
      openAPI.getPaths().forEach((s, pathItem) -> {
        newPath.put(s.replace("_", ".").replace("*{", "{"), pathItem);
      });
      openAPI.setPaths(newPath);
      body.setSpec(Json.mapper().readTree(mock.getSwagger()));
      body.setDateUpdated(mock.getUpdatedDate());
      body.setUpdatedBy(
          CreatorMockVmodel.builder().userId(this.getAuthenticationProfileModel().get_id())
              .username(this.getAuthenticationProfileModel().getUsername()).build());

      return ResponseEntity.ok(body);
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * for checking database health
   *
   * @return ResponseEntity
   */
  @GetMapping(value = "/health",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> healthCheck() {
    try {
      BsonDocument b = new BsonDocument();
      b.put("ping", new BsonString("1"));
      Document doc = mongoTemplate.getDb().runCommand(b);
      if (doc.get("ok").equals(1.0)) {
        return ResponseEntity.ok("up");
      }
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("down");
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  /**
   * delete mock base on id
   *
   * @param id id from mock collection
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
  @DeleteMapping(value = "/mocks/{id}/delete",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> deleteMock(@PathVariable String id, HttpServletRequest request) {
    try {
      this.mockInterface.deleteById(id, this.getAuthenticationProfileModel());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * get spec mock
   *
   * @param id      id from mock collection
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @GetMapping(value = "/mocks/{id}/spec", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getSpecMock(@PathVariable String id, HttpServletRequest request) {
    try {
      MockEntities mock = this.mockInterface.getById(id, this.getAuthenticationProfileModel());
      MockVmodel mockResponseVmodel = new MockVmodel();
      mockResponseVmodel.setSpec(Json.mapper().readTree(mock.getSwagger()));
      return ResponseEntity.ok(mockResponseVmodel.getSpec());
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * update mock
   *
   * @param id      id from mock collection
   * @param body    mock payload data
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
  @PutMapping(value = "/mocks/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateMockById(@PathVariable String id,
      @Valid @RequestBody MockVmodel body, HttpServletRequest request) {
    try {
      MockEntities mock = this.mockInterface
          .updateByID(id, body, this.getAuthenticationProfileModel());
      body.setId(mock.getId());
      body.setSpec(Json.mapper().readTree(mock.getSwagger()));
      body.setDateUpdated(mock.getUpdatedDate());
      return ResponseEntity.ok(body);
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * mocks pagination base on user access permission
   *
   * @param pageable Spring data pageable
   * @param q        query data example q=name:fahmi => meaning field name with value fahmi
   * @param request  HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
  @GetMapping(value = "/mocks/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getMocksPagination(
      Pageable pageable,
      @RequestParam(value = "q", required = false) String q, HttpServletRequest request) {
    try {
      MockEntitiesPage pagingVmodel = this.mockInterface
          .paging(pageable, q, this.getAuthenticationProfileModel());
      return ResponseEntity.ok(pagingVmodel);
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }
}
