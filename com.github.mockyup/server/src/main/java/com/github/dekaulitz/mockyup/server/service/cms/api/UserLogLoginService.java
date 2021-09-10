package com.github.dekaulitz.mockyup.server.service.cms.api;

import com.github.dekaulitz.mockyup.server.db.entities.UserLogLoginEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.Mandatory;
import com.github.dekaulitz.mockyup.server.model.param.GetUserLogLoginParam;
import com.github.dekaulitz.mockyup.server.service.BaseCrudService;
import com.mongodb.client.result.DeleteResult;
import javax.validation.constraints.NotBlank;

public interface UserLogLoginService extends
    BaseCrudService<UserLogLoginEntity, GetUserLogLoginParam> {

  UserLogLoginEntity findByJti(@NotBlank String jti);

  DeleteResult deleteByParameter(@NotBlank GetUserLogLoginParam getUserLogLoginParam);

  void logLogin(AuthProfileModel authProfileModel, Mandatory mandatory) throws ServiceException;
}
