package com.github.dekaulitz.mockyup.domain.users.base;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 * user interface for user modification
 */
public interface UserInterface {

  /**
   * for registering new user
   *
   * @param vmodel                     {@link RegistrationVmodel} user data
   * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth profile
   * @return UserEntities user data profile
   * @throws DuplicateDataEntry when username alrady exist will thrown
   */
  UserEntities addUser(RegistrationVmodel vmodel,
      AuthenticationProfileModel authenticationProfileModel) throws DuplicateDataEntry;

  /**
   * for deleting the user
   *
   * @param userId                     {@link String} id from user collection
   * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth profile
   * @throws NotFoundException when user not found
   */
  void deleteUser(String userId, AuthenticationProfileModel authenticationProfileModel)
      throws NotFoundException;

  /**
   * user pagination
   *
   * @param pageable {@link Pageable} spring data pagiable
   * @param q        {@link String} query data example q=name:fahmi => meaning field name with value
   *                 fahmi
   * @return UserEntitiesPage
   */
  UserEntitiesPage paging(Pageable pageable, String q);

  /**
   * searching all user base on regex username
   *
   * @param username                   {@link String} username user
   * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile
   * @return List<UserEntities>
   */
  List<UserEntities> listUsers(String username,
      AuthenticationProfileModel authenticationProfileModel);

  /**
   * get user detail
   *
   * @param id {@link String} id from user collection
   * @return UserEntities user data
   * @throws NotFoundException if user is not found will thrown
   */
  UserEntities getUserById(String id) throws NotFoundException;

  /**
   * for update the user
   *
   * @param vmodel {@link UpdateUserVmodel} update user data
   * @param id     {@link String} id from user collection
   * @return UserEntities user data profile
   * @throws DuplicateDataEntry when username that will updated is already used by other user
   * @throws NotFoundException  when id from user collection is not found
   */
  UserEntities updateUser(UpdateUserVmodel vmodel, String id)
      throws DuplicateDataEntry, NotFoundException;
}
