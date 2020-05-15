package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.models.helper.MockExample;
import com.github.dekaulitz.mockyup.repositories.MockRepository;
import com.github.dekaulitz.mockyup.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MockModel extends BaseModel<MockEntities, MockVmodel> {

    @Autowired
    private final MockRepository mockRepository;
    @Autowired
    private final MongoTemplate mongoTemplate;

    public MockModel(MockRepository mockRepository, MongoTemplate mongoTemplate) {
        this.mockRepository = mockRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<MockEntities> all() {
        return this.mockRepository.findAll();
    }

    @Override
    public MockEntities getById(String id) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        return mockEntities.get();
    }

    @Override
    public MockEntities save(MockVmodel view) throws InvalidMockException {
        try {
            MockEntities mockEntities = new MockEntities();
            this.setMockEntity(view, mockEntities);
            return this.mockRepository.save(mockEntities);
        } catch (JsonProcessingException e) {
            throw new InvalidMockException("invalid mock exception " + e.getMessage());
        }
    }

    @Override
    public MockEntities updateByID(String id, MockVmodel view) throws NotFoundException, JsonProcessingException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        this.setMockEntity(view, mockEntities.get());
        mockRepository.save(mockEntities.get());
        return mockEntities.get();
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        mockRepository.deleteById(mockEntities.get().getId());
    }

    @Override
    public MockEntitiesPage paging(Pageable pageable, String q) {
        MockEntitiesPage basePage = new MockEntitiesPage();
        basePage.setConnection(mongoTemplate);
        basePage.addCriteria(q);
        basePage.setPageable(pageable).build(MockEntities.class);
        return basePage;
    }


    public MockExample getMockMocking(HttpServletRequest request, String path, String id, String body) throws NotFoundException, JsonProcessingException, UnsupportedEncodingException, InvalidMockException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent())
            throw new NotFoundException("data not found");
        String[] originalPathUri = path.split("\\?");
        String[] paths = originalPathUri[0].split("/");
        OpenAPI openAPI = JsonMapper.mapper().readValue(mockEntities.get().getSpec(), OpenAPI.class);
        //pathitem is exist or without path parameter clean url
        PathItem item = openAPI.getPaths().get(path);
        if (item != null) {
            String openApiPath = path.replace("_", ".");
            String[] openAPIPaths = openApiPath.split("/");
            return getMockResponse(item, request, body, openAPIPaths, paths);
        }
        //path item with path parameter we should check every endpoint that related with data
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String s = entry.getKey();
            PathItem pathItem = entry.getValue();
            String openApiPath = s.replace("_", ".");
            String[] openAPIPaths = openApiPath.split("/");
            String[] regexPath = new String[paths.length];
            // every targeting mocks uri should has same length
            if (openAPIPaths.length == paths.length) {
                // converting request path to original for searching data
                for (int i = 0; i < openAPIPaths.length; i++) {
                    //checking path parameter
                    if (!openAPIPaths[i].equals(paths[i])) {
                        if (openAPIPaths[i].contains("*")) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    } else {
                        //this is for checking wildcard with path parameter
                        if (!paths[i].contains("*") && openAPIPaths[i].equals(paths[i])) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    }
                }
                //check if current openapi path equal with regexpath (path)
                if (openApiPath.equals(String.join("/", regexPath)))
                    return getMockResponse(pathItem, request, body, openAPIPaths, paths);
            }
        }
        return null;
    }
}
