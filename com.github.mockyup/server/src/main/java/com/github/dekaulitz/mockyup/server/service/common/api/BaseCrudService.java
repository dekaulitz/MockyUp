package com.github.dekaulitz.mockyup.server.service.common.api;

import com.github.dekaulitz.mockyup.server.db.entities.BaseMongo;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import java.util.List;
import org.springframework.data.mongodb.core.query.Query;

public interface BaseCrudService<T extends BaseMongo> {

  T getById(String id, Class<T> entityClass) throws ServiceException;

  T findOne(Query query, Class<T> entityClass) throws ServiceException;

  void delete(T t) throws ServiceException;

  void deleteById(String id) throws ServiceException;

  List<T> getAll(Query query, Class<T> entityClass);

  T update(String id, T t) throws ServiceException;

  T update(T t) throws ServiceException;

  T save(T t) throws ServiceException;

  Boolean isExists(Query query, Class<T> entityClass);
}
