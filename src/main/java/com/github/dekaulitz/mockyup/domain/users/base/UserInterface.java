package com.github.dekaulitz.mockyup.domain.users.base;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserInterface {
    /**
     * @param vmodel
     * @param authenticationProfileModel
     * @return
     * @throws DuplicateDataEntry
     */
    UserEntities addUser(RegistrationVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws DuplicateDataEntry;

    /**
     * @param userId
     * @param authenticationProfileModel
     * @throws DuplicateDataEntry
     * @throws NotFoundException
     */
    void deleteUser(String userId, AuthenticationProfileModel authenticationProfileModel) throws DuplicateDataEntry, NotFoundException;

    /**
     * @param pageable
     * @param q
     * @return
     */
    UserEntitiesPage paging(Pageable pageable, String q);

    /**
     * @param username
     * @param authenticationProfileModel
     * @return
     */
    List<UserEntities> listUsers(String username, AuthenticationProfileModel authenticationProfileModel);

    /**
     * @param id
     * @return
     * @throws NotFoundException
     */
    UserEntities getUserById(String id) throws NotFoundException;

    /**
     * @param vmodel
     * @param id
     * @return
     * @throws DuplicateDataEntry
     * @throws NotFoundException
     */
    UserEntities updateUser(UpdateUserVmodel vmodel, String id) throws DuplicateDataEntry, NotFoundException;
}
