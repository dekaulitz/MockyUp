package com.github.dekaulitz.mockyup.server.service;

import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.PageableParam;
import java.util.List;

public interface BaseCrudService<T, P extends PageableParam> {

  T getById(String id) throws ServiceException;

  void delete(T t) throws ServiceException;

  List<T> getAll(P p);

  T update(T t) throws ServiceException;

  T save(T t) throws ServiceException;
}
