package com.github.dekaulitz.mockyup.domain.users.models;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.domain.users.base.UserInterface;
import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserModel implements UserInterface {

  @Autowired
  private final UserRepository userRepository;


  public UserModel(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * for registering new user
   *
   * @param vmodel                     {@link RegistrationVmodel} user data
   * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth profile
   * @return UserEntities user data profile
   * @throws DuplicateDataEntry when username alrady exist will thrown
   */
  @Override
  public UserEntities addUser(RegistrationVmodel vmodel,
      AuthenticationProfileModel authenticationProfileModel) throws DuplicateDataEntry {
    boolean isExist = this.userRepository.existsByUsername(vmodel.getUsername());
    if (isExist) {
      throw new DuplicateDataEntry(ResponseCode.USER_ALREADY_EXIST);
    }
    UserEntities userEntities = new UserEntities();
    userEntities.setUsername(vmodel.getUsername());
    userEntities.setPassword(Hash.hashing(vmodel.getPassword()));
    userEntities.setAccessList(vmodel.getAccessList());
    userEntities.setUpdatedDate(new Date());
    return this.userRepository.save(userEntities);
  }

  /**
   * for deleting the user
   *
   * @param userId                     {@link String} id from user collection
   * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth profile
   * @throws NotFoundException when user not found
   */
  @Override
  public void deleteUser(String userId, AuthenticationProfileModel authenticationProfileModel)
      throws NotFoundException {
    Optional<UserEntities> userEntities = this.userRepository.findById(userId);
    if (!userEntities.isPresent()) {
      throw new NotFoundException(ResponseCode.USER_NOT_FOUND);
    }
    this.userRepository.delete(userEntities.get());
  }

  /**
   * user pagination
   *
   * @param pageable {@link Pageable} spring data pagiable
   * @param q        {@link String} query data example q=name:fahmi => meaning field name with value
   *                 fahmi
   * @return UserEntitiesPage
   */
  @Override
  public UserEntitiesPage paging(Pageable pageable, String q) {
    return this.userRepository.paging(pageable, q);
  }

  @Override
  public List<UserEntities> listUsers(String username,
      AuthenticationProfileModel authenticationProfileModel) {
    return this.userRepository.getUserListByUserName(username, authenticationProfileModel.get_id());
  }

  /**
   * get user detail
   *
   * @param id {@link String} id from user collection
   * @return UserEntities user data
   * @throws NotFoundException if user is not found will thrown
   */
  @Override
  public UserEntities getUserById(String id) throws NotFoundException {
    Optional<UserEntities> userEntities = this.userRepository.findById(id);
    if (!userEntities.isPresent()) {
      throw new NotFoundException(ResponseCode.USER_NOT_FOUND);
    }
    return userEntities.get();
  }

  /**
   * for update the user
   *
   * @param vmodel {@link UpdateUserVmodel} update user data
   * @param id     {@link String} id from user collection
   * @return UserEntities user data profile
   * @throws DuplicateDataEntry when username that will updated is already used by other user
   * @throws NotFoundException  when id from user collection is not found
   */
  @Override
  public UserEntities updateUser(UpdateUserVmodel vmodel, String id)
      throws DuplicateDataEntry, NotFoundException {
    UserEntities userExist = this.userRepository.findFirstByUsername(vmodel.getUsername());
    if (userExist != null && !userExist.getId().equals(id)) {
      throw new DuplicateDataEntry(ResponseCode.USER_ALREADY_EXIST);
    }
    Optional<UserEntities> userEntities = this.userRepository.findById(id);
    if (!userEntities.isPresent()) {
      throw new NotFoundException(ResponseCode.USER_NOT_FOUND);
    }
    if (vmodel.getPassword().isEmpty()) {
      vmodel.setPassword(userEntities.get().getPassword());
    } else {
      vmodel.setPassword(Hash.hashing(vmodel.getPassword()));
    }
    UserEntities userEntitiesUpdate = UserEntities.builder()
        .username(vmodel.getUsername()).id(id)
        .password(vmodel.getPassword()).updatedDate(new Date())
        .accessList(vmodel.getAccessList()).build();
    return this.userRepository.save(userEntitiesUpdate);
  }
}

