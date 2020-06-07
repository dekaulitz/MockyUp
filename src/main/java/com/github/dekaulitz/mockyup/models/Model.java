package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.db.repositories.paging.AbstractPage;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Model<T, V> {
    List<T> all();

    T getById(String id) throws NotFoundException;

    T save(MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException;

    T updateByID(String id, MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException, JsonProcessingException, InvalidMockException;

    AbstractPage<T> paging(Pageable pageable, String q, AuthenticationProfileModel userId);

    void deleteById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;
}
