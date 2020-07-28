package com.github.dekaulitz.mockyup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.base.controller.BaseController;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.base.MockInterface;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
     * @return
     * @throws IOException
     * @desc showing swagger doc
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
     * @param path
     * @param id
     * @param body
     * @return
     */
    @RequestMapping(value = "/mocks/mocking/{id}", method = {RequestMethod.OPTIONS, RequestMethod.DELETE,
            RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.PUT,
            RequestMethod.TRACE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> mockingPath(@NonNull @RequestParam(value = "path") String path,
                                              @PathVariable String id, @RequestBody(required = false) String body,
                                              HttpServletRequest request) {
        MockHelper mock;
        try {
            mock = this.mockInterface.getMockMocking(request, path, id, body);
            if (mock != null)
                return this.generateMockResponseEntity(mock);
            return new ResponseEntity<>("no example mock found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getUserMocks(@PathVariable String id) {
        return ResponseEntity.ok(this.mockInterface.getUsersListOfMocks(id));
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/detailWithAccess",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getCurrentAccess(@PathVariable String id, HttpServletRequest request) {
        try {
            List<DtoMockupDetailVmodel> mockDetailWithAccess = this.mockInterface.getDetailMockUpIdByUserAccess(id,
                    this.getAuthenticationProfileModel());
            if (mockDetailWithAccess.isEmpty()) {
                throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
            }
            return ResponseEntity.ok(mockDetailWithAccess.get(0));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/histories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getMockHistories(@PathVariable String id) {
        return ResponseEntity.ok(this.mockInterface.getMockHistories(id));
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @PutMapping(value = "/mocks/{id}/addUser",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addUserAccess(@PathVariable String id, @Valid @RequestBody AddUserAccessVmodel vmodel,
                                                HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.mockInterface.addUserAccessOnMock(id, vmodel, this.getAuthenticationProfileModel()));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @DeleteMapping(value = "/mocks/{id}/remove/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> removeUserAccessOnMock(@PathVariable String id, @PathVariable String userId, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.mockInterface.removeAccessUserOnMock(id, userId, this.getAuthenticationProfileModel()));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }


    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/histories/{historyId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getSpecHistory(@PathVariable String id, @PathVariable String historyId, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.mockInterface.geMockHistoryId(id, historyId, this.getAuthenticationProfileModel()));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    /**
     * @param body
     * @return
     * @throws JsonProcessingException
     * @desc storing the mocks
     */
    @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
    @PostMapping(value = "/mocks/store")
    public ResponseEntity<Object> storeMocksEntity(@Valid @RequestBody MockVmodel body, HttpServletRequest request) {
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

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @GetMapping(value = "/health",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> healthCheck() {
        try {
            BsonDocument b = new BsonDocument();
            b.put("ping", new BsonString("1"));
            Document doc = mongoTemplate.getDb().runCommand(b);
            if (doc.get("ok").equals(1.0)) return ResponseEntity.ok("up");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("down");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @param id
     * @return
     * @desc delete mock by id
     */
    @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
    @DeleteMapping(value = "/mocks/{id}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteByMockId(@PathVariable String id, HttpServletRequest request) {
        try {
            this.mockInterface.deleteById(id, this.getAuthenticationProfileModel());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/spec", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMockSpecById(@PathVariable String id, HttpServletRequest request) {
        try {
            MockEntities mock = this.mockInterface.getById(id, this.getAuthenticationProfileModel());
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setSpec(Json.mapper().readTree(mock.getSwagger()));
            return ResponseEntity.ok(mockResponseVmodel.getSpec());
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
    @PutMapping(value = "/mocks/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMockById(@PathVariable String id, @Valid @RequestBody MockVmodel body, HttpServletRequest request) {
        try {
            MockEntities mock = this.mockInterface.updateByID(id, body, this.getAuthenticationProfileModel());
            body.setId(mock.getId());
            body.setSpec(Json.mapper().readTree(mock.getSwagger()));
            body.setDateUpdated(mock.getUpdatedDate());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMocksPagination(
            Pageable pageable,
            @RequestParam(value = "q", required = false) String q, HttpServletRequest request) {
        try {
            MockEntitiesPage pagingVmodel = this.mockInterface.paging(pageable, q, this.getAuthenticationProfileModel());
            return ResponseEntity.ok(pagingVmodel);
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }
}
