package com.github.dekaulitz.mockyup.models;

import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.entities.UserEntities;
import com.github.dekaulitz.mockyup.errorhandlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.repositories.UserRepository;
import com.github.dekaulitz.mockyup.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.JwtManager;
import com.github.dekaulitz.mockyup.vmodels.AccessVmodel;
import com.github.dekaulitz.mockyup.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.vmodels.UserLoginVmodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
public class UserModel {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final MongoTemplate mongoTemplate;


    public UserModel(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public UserEntities addUser(RegistrationVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws DuplicateDataEntry {
        boolean isExist = this.userRepository.existsByUsername(vmodel.getUsername());
        if (isExist) {
            throw new DuplicateDataEntry("user already exist");
        }
        UserEntities userEntities = new UserEntities();
        userEntities.setUsername(vmodel.getUsername());
        userEntities.setPassword(Hash.hashing(vmodel.getPassword()));
        userEntities.setAccessList(vmodel.getAccessList());
        return this.userRepository.save(userEntities);
    }

    public AccessVmodel doLogin(UserLoginVmodel vmodel) throws UnathorizedAccess, UnsupportedEncodingException {
        UserEntities userExist = this.userRepository.findFirstByUsername(vmodel.getUsername());
        if (userExist == null) {
            throw new UnathorizedAccess("invalid username or password");
        }
        boolean isAuthenticated = Hash.verifyHash(vmodel.getPassword(), userExist.getPassword());
        if (!isAuthenticated) throw new UnathorizedAccess("invalid username or password");
        String token = JwtManager.generateToken(userExist);
        return AccessVmodel.builder().username(userExist.getUsername())
                .accessMenus(userExist.getAccessList())
                .token(token).build();
    }

    public UserEntitiesPage paging(Pageable pageable, String q) {
        UserEntitiesPage basePage = new UserEntitiesPage();
        basePage.setConnection(mongoTemplate);
        //add search query param
        basePage.addCriteria(q);
        //selected field
        basePage.getQuery().fields().include("_id").include("username");
        //add additional criteria or custom criteria
//        basePage.addAdditionalCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is("5ec41562b5a0ae5108a31c1d")));
        basePage.setPageable(pageable).build(UserEntities.class);
        return basePage;
    }

    public List<UserEntities> listUsers(String q) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").regex(".*" + q + ".*", "i")).limit(25);
        query.fields().include("_id").include("username");
        return this.mongoTemplate.find(query, UserEntities.class);
    }

    public UserEntities getUserById(String id) throws NotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        query.fields().include("_id").include("username").include("accessList");
        UserEntities userEntities = this.mongoTemplate.findOne(query, UserEntities.class);
        if (userEntities == null)
            throw new NotFoundException("user not found");
        return userEntities;
    }

    public UserEntities updateUser(UpdateUserVmodel vmodel, String id) throws DuplicateDataEntry, NotFoundException {
        UserEntities userExist = this.userRepository.findFirstByUsername(vmodel.getUsername());
        if (userExist != null && !userExist.getId().equals(id)) {
            throw new DuplicateDataEntry("user already exist");
        }
        Optional<UserEntities> userEntities = this.userRepository.findById(id);
        if (!userEntities.isPresent()) throw new NotFoundException("user not found");
        if (vmodel.getPassword().isEmpty()) vmodel.setPassword(userEntities.get().getPassword());
        else vmodel.setPassword(Hash.hashing(vmodel.getPassword()));
        UserEntities userEntitiesUpdate = UserEntities.builder()
                .username(vmodel.getUsername()).id(id)
                .password(vmodel.getPassword())
                .accessList(vmodel.getAccessList()).build();
        return this.userRepository.save(userEntitiesUpdate);
    }
}

