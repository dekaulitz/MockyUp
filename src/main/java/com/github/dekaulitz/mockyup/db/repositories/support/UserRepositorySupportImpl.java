package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositorySupportImpl implements UserRepositorySupport {
    @Autowired
    private final MongoTemplate mongoTemplate;

    public UserRepositorySupportImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * @param pageable Spring data pageable
     * @param q        query data like example q=firstname:fahmi mean field firstname with value fahmi
     * @return UserEntitiesPage
     */
    @Override
    public UserEntitiesPage paging(Pageable pageable, String q) {
        UserEntitiesPage basePage = new UserEntitiesPage();
        basePage.setConnection(mongoTemplate);
        //add search query param
        basePage.addCriteria(q);
        //selected field
        basePage.getQuery().fields().include("_id").include("username").include("updatedDate");
        //add additional criteria or custom criteria
//        basePage.addAdditionalCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is("5ec41562b5a0ae5108a31c1d")));
        basePage.setPageable(pageable).build(UserEntities.class);
        return basePage;
    }

    /**
     * geting all users base on username
     *
     * @param username      parameter that will searching on user collection
     * @param excludeUserId user id that not included for query
     * @return List<UserEntities>
     */
    @Override
    public List<UserEntities> getUserListByUserName(String username, String excludeUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").regex(".*" + username + ".*", "i"))
                .addCriteria(Criteria.where("id").ne(new ObjectId(excludeUserId))).limit(25);
        query.fields().include("id").include("username");
        return this.mongoTemplate.find(query, UserEntities.class);
    }
}
