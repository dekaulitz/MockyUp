package com.github.dekaulitz.mockyup.models.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.db.repositories.paging.AbstractPage;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseMock<T, V> {
    List<T> all();

    T getById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    T save(V view, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException;

    T updateByID(String id, V view, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException, JsonProcessingException, InvalidMockException;

    AbstractPage<T> paging(Pageable pageable, String q, AuthenticationProfileModel userId);

    void deleteById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;
}
