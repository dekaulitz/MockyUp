package com.github.dekaulitz.mockyup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.models.MockModel;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
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
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
//@Log4j2
public class MockControllers extends BaseController {
    @Autowired
    private final MongoTemplate mongoTemplate;

    @Autowired
    private final MockModel mockModel;

    public MockControllers(LogsMapper logsMapper, MongoTemplate mongoTemplate, MockModel mockModel) {
        super(logsMapper);
        this.mongoTemplate = mongoTemplate;
        this.mockModel = mockModel;
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
     * @param request
     * @return
     * @throws JsonProcessingException
     * @desc mocking the uri that registerion on mockyup
     */
    @RequestMapping(value = "/mocks/mocking/{id}", method = {RequestMethod.OPTIONS, RequestMethod.DELETE,
            RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.PUT,
            RequestMethod.TRACE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity mockingPath(@NonNull @RequestParam(value = "path") String path,
                                      @PathVariable String id, @RequestBody(required = false) String body,
                                      HttpServletRequest request, HttpServletResponse response) {
        MockHelper mock = null;
        try {
            mock = this.mockModel.getMockMocking(request, path, id, body);
            if (mock != null)
                return this.generateMockResponseEntity(mock);
            return new ResponseEntity<>("no example mock found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getUserMocks(@PathVariable String id) {
        return ResponseEntity.ok(this.mockModel.getUsersListOfMocks(id));
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/detailWithAccess",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getCurrentAccess(@PathVariable String id) {
        List<DtoMockupDetailVmodel> mockDetailWithAccess = this.mockModel.getDetailMockUpIdByUserAccess(id, this.getAuthenticationProfileModel());
        if (mockDetailWithAccess.isEmpty()) {
            throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
        }
        return ResponseEntity.ok(mockDetailWithAccess.get(0));
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/histories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getMockHistories(@PathVariable String id) {
        return ResponseEntity.ok(this.mockModel.getMockHistories(id));
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @PutMapping(value = "/mocks/{id}/addUser",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addUserAccess(@PathVariable String id, @RequestBody AddUserAccessVmodel vmodel) {
        try {
            return ResponseEntity.ok(this.mockModel.addUserAccessOnMock(id, vmodel, this.getAuthenticationProfileModel()));
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @DeleteMapping(value = "/mocks/{id}/remove/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> removeUserAccessOnMock(@PathVariable String id, @PathVariable String userId) {
        try {
            return ResponseEntity.ok(this.mockModel.removeAccessUserOnMock(id, userId, this.getAuthenticationProfileModel()));
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }


    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/histories/{historyId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getSpecHistory(@PathVariable String id, @PathVariable String historyId) {
        try {
            return ResponseEntity.ok(this.mockModel.geMockHistoryId(id, historyId, this.getAuthenticationProfileModel()));
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
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
    public ResponseEntity storeMocksEntity(@RequestBody MockVmodel body) {
        LOGGER.info("{}", body);
        try {
            MockEntities mock = this.mockModel.save(body, this.getAuthenticationProfileModel());
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
            return this.handlingErrorResponse(e);
        }
    }

    @GetMapping(value = "/health",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity healthCheck() {
        try {
            BsonDocument b = new BsonDocument();
            b.put("ping", new BsonString("1"));
            Document doc = mongoTemplate.getDb().runCommand(b);
            if (doc.get("ok").equals(1.0)) {
                return ResponseEntity.ok("up");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("down");
            }
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
    public ResponseEntity deleteByMockId(@PathVariable String id) {
        try {
            this.mockModel.deleteById(id, this.getAuthenticationProfileModel());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/spec", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMockSpecById(@PathVariable String id) {
        try {
            MockEntities mock = this.mockModel.getById(id, this.getAuthenticationProfileModel());
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setSpec(Json.mapper().readTree(mock.getSwagger()));
            return ResponseEntity.ok(mockResponseVmodel.getSpec());
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
    @PutMapping(value = "/mocks/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateMockById(@PathVariable String id, @RequestBody MockVmodel body) {
        try {
            MockEntities mock = this.mockModel.updateByID(id, body, this.getAuthenticationProfileModel());
            body.setId(mock.getId());
            body.setSpec(Json.mapper().readTree(mock.getSwagger()));
            body.setDateUpdated(mock.getUpdatedDate());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMocksPagination(
            Pageable pageable,
            @RequestParam(value = "q", required = false) String q) {
        try {
            MockEntitiesPage pagingVmodel = this.mockModel.paging(pageable, q, this.getAuthenticationProfileModel());
            return ResponseEntity.ok(pagingVmodel);
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }
}
