package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.repositories.paging.AbstractPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Model<T, V> {
    List<T> all();

    T getById(String id) throws NotFoundException;

    T save(V view) throws InvalidMockException;

    T updateByID(String id, V view) throws NotFoundException, JsonProcessingException;

    AbstractPage<T> paging(Pageable pageable, String q);

    void deleteById(String id) throws NotFoundException;
}
