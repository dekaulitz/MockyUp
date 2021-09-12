package com.github.dekaulitz.mockyup.server.service.common.impl;

import com.github.dekaulitz.mockyup.server.db.entities.BaseMongo;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.service.common.api.BaseCrudService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("baseCrud")
@Slf4j
public abstract class BaseCrudServiceImpl<T extends BaseMongo> implements
    BaseCrudService<T> {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  @Nullable
  public T getById(String id, Class<T> entityClass) throws ServiceException {
    return mongoTemplate.findById(new ObjectId(id), entityClass);
  }

  @Override
  public void delete(T t) throws ServiceException {

  }

  public List<T> getAll(Query query, Class<T> entityClass) {
    return mongoTemplate.find(query, entityClass);
  }

  @Override
  @Retryable(value = {
      OptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
  public T update(String id, @NotNull T t) throws ServiceException {
    T data = this.getById(id, (Class<T>) t.getClass());
    t.setVersion(data.getVersion());
    return mongoTemplate.save(t);
  }

  @Override
  @Retryable(value = {
      OptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
  public T save(@Valid T t) throws ServiceException {
    return mongoTemplate.save(t);
  }

  @Override
  public T findOne(Query query, Class<T> entityClass) throws ServiceException {
    return mongoTemplate.findOne(query, entityClass);
  }

  @Override
  public T update(@Validated @Valid T t) throws ServiceException {
    return mongoTemplate.save(t);
  }

  @Override
  public Boolean isExists(Query query, Class<T> entityClass) {
    return mongoTemplate.exists(query, entityClass);
  }
}
