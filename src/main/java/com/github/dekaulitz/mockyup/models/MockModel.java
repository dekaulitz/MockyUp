package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.models.helper.MockExample;
import com.github.dekaulitz.mockyup.repositories.MockRepositories;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MockModel extends BaseModel<MockEntities, MockVmodel> {

    @Autowired
    private final MockRepositories mockRepositories;

    public MockModel(MockRepositories mockRepositories) {
        this.mockRepositories = mockRepositories;
    }

    @Override
    public List<MockEntities> all() {
        return this.mockRepositories.findAll();
    }

    @Override
    public MockEntities getById(String id) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepositories.findById(id);
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
            return this.mockRepositories.save(mockEntities);
        } catch (JsonProcessingException e) {
            throw new InvalidMockException("invalid mock exception " + e.getMessage());
        }
    }

    @Override
    public MockEntities updateByID(String id, MockVmodel view) throws NotFoundException, JsonProcessingException {
        Optional<MockEntities> mockEntities = mockRepositories.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        this.setMockEntity(view, mockEntities.get());
        mockRepositories.save(mockEntities.get());
        return mockEntities.get();
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepositories.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        mockRepositories.deleteById(mockEntities.get().getId());
    }

    public MockExample getMockMocking(HttpServletRequest request, String path, String _id, String body) throws Exception {
        Optional<MockEntities> mockEntities = this.mockRepositories.findById(_id);
        if (!mockEntities.isPresent())
            throw new NotFoundException("data not found");
        String[] originalPathUri = path.split("\\?");
        OpenAPI openAPI = JsonMapper.mapper().readValue(mockEntities.get().getSpec(), OpenAPI.class);
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String s = entry.getKey();
            PathItem pathItem = entry.getValue();
            String openApiPath = s.replace("_", ".");
            String[] openAPIPaths = openApiPath.split("/");
            String[] paths = originalPathUri[0].split("/");
            String[] regexPath = new String[paths.length];
            // every targeting mocks uri should has same length
            if (openAPIPaths.length == paths.length) {
                // converting request path to original for searching data
                for (int i = 0; i < openAPIPaths.length; i++) {
                    if (!openAPIPaths[i].equals(paths[i])) {
                        if (openAPIPaths[i].contains("*")) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    } else {
                        regexPath[i] = openAPIPaths[i];
                    }
                }
                if (openApiPath.equals(String.join("/", regexPath)))
                    return getMockResponse(pathItem, request, body, openAPIPaths, paths);
                else {
                    return getMockResponse(pathItem, request, body, openAPIPaths, paths);
                }
            }
        }
        return null;
    }
}
