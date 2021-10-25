package com.github.dekaulitz.mockyup.server.service.cms.impl;

import com.github.dekaulitz.mockyup.server.db.entities.UserLogLoginEntity;
import com.github.dekaulitz.mockyup.server.db.query.UserLogLoginQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.param.GetUserLogLoginParam;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserLogLoginService;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.common.impl.BaseCrudServiceImpl;
import com.mongodb.client.result.DeleteResult;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserLogLoginServiceImpl extends
    BaseCrudServiceImpl<UserLogLoginEntity> implements
    UserLogLoginService {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private CacheService cacheService;

  @Override
  public void delete(UserLogLoginEntity userLogLoginEntity) throws ServiceException {
    UserLogLoginQuery userLogLoginQuery = new UserLogLoginQuery();
    userLogLoginQuery.jti(userLogLoginEntity.getJti());
    mongoTemplate.findAndRemove(userLogLoginQuery.getQuery(), UserLogLoginEntity.class);
    String cacheKey = CacheConstants.AUTH_PREFIX + userLogLoginEntity.getJti();
    cacheService.deleteCache(cacheKey);
  }

  @Override
  public List<UserLogLoginEntity> getAll(GetUserLogLoginParam getUserLogLoginParam) {
    UserLogLoginQuery userLogLoginQuery = new UserLogLoginQuery();
    userLogLoginQuery.buildQuery(getUserLogLoginParam);
    return mongoTemplate.find(userLogLoginQuery.getQueryWithPaging(), UserLogLoginEntity.class);
  }


  @Override
  public UserLogLoginEntity save(@Valid @NotNull UserLogLoginEntity userLogLoginEntity)
      throws ServiceException {
    userLogLoginEntity.setCreatedDate(new Date());
    return mongoTemplate.save(userLogLoginEntity);
  }

  @Override
  public UserLogLoginEntity findByJti(@NotBlank String jti) {
    UserLogLoginQuery userLogLoginQuery = new UserLogLoginQuery();
    userLogLoginQuery.jti(jti);
    return mongoTemplate.findOne(userLogLoginQuery.getQuery(), UserLogLoginEntity.class);
  }

  @Override
  public DeleteResult deleteByParameter(GetUserLogLoginParam getUserLogLoginParam) {
    UserLogLoginQuery userLogLoginQuery = new UserLogLoginQuery();
    userLogLoginQuery.buildQuery(getUserLogLoginParam);
    return mongoTemplate.remove(userLogLoginQuery.getQuery(), UserLogLoginEntity.class);
  }

  @Override
  public UserLogLoginEntity deleteByJtiOrUserId(@NotBlank String jtIOrUserId) {
    UserLogLoginQuery userLogLoginQuery = new UserLogLoginQuery();
    userLogLoginQuery.jtiOrUserId(jtIOrUserId);
    return mongoTemplate.findAndRemove(userLogLoginQuery.getQuery(), UserLogLoginEntity.class);
  }

  @Override
  public void deleteById(String id) throws ServiceException {
    UserLogLoginEntity entity = getById(id, UserLogLoginEntity.class);
    if (entity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND,
          "user log not found id: " + id);
    }
    delete(entity);
  }

  @Override
  public void logLogin(AuthProfileModel authProfileModel, MandatoryModel mandatoryModel)
      throws ServiceException {
    UserLogLoginEntity userLogLoginEntity = UserLogLoginEntity.builder()
        .jti(authProfileModel.getJti())
        .userId(authProfileModel.getId())
        .mandatoryModel(mandatoryModel)
        .build();
    save(userLogLoginEntity);
  }
}
