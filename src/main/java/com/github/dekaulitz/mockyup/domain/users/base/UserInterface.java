package com.github.dekaulitz.mockyup.domain.users.base;

import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.infrastructure.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserInterface {
    UserEntities addUser(RegistrationVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws DuplicateDataEntry;

    void deleteUser(String userId, AuthenticationProfileModel authenticationProfileModel) throws DuplicateDataEntry;

    UserEntitiesPage paging(Pageable pageable, String q);

    List<UserEntities> listUsers(String username, AuthenticationProfileModel authenticationProfileModel);

    UserEntities getUserById(String id) throws NotFoundException;

    UserEntities updateUser(UpdateUserVmodel vmodel, String id) throws DuplicateDataEntry, NotFoundException;
}
