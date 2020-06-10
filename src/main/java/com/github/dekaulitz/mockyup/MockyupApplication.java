package com.github.dekaulitz.mockyup;

import com.github.dekaulitz.mockyup.db.entities.MockCreatorEntities;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.entities.UserMocksEntities;
import com.github.dekaulitz.mockyup.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class MockyupApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockyupApplication.class);
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final MockRepository mockRepository;

    public MockyupApplication(UserRepository userRepository, MockRepository mockRepository) {
        this.userRepository = userRepository;
        this.mockRepository = mockRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MockyupApplication.class, args);
    }

    @Override
    //add script user injecetion and get all mock that has not users and mark root as the user
    public void run(String... args) throws Exception {
        // injecting user root
        UserEntities userEntities = this.userRepository.findFirstByUsername("root");
        if (userEntities == null) {
            LOGGER.info("user not found will create new user root");
            UserEntities rootUser = new UserEntities();
            rootUser.setUsername("root");
            rootUser.setPassword(Hash.hashing("root"));
            rootUser.setAccessList(Arrays.asList(Role.MOCKS_READ_WRITE.name(), Role.USERS_READ_WRITE.name()));
            this.userRepository.save(rootUser);
            //get all mocks that has not users on the mock
            List<MockEntities> mocksHasNoUsers = this.mockRepository.getMocksHasNoUsers(null);
            mocksHasNoUsers.forEach(mockEntities -> {
                List<UserMocksEntities> userMocksEntitiesList = new ArrayList<>();
                userMocksEntitiesList.add(UserMocksEntities.builder()
                        .userId(rootUser.getId())
                        .access(Role.MOCKS_READ_WRITE.toString())
                        .build());
                MockEntities updateMockenties = MockEntities.builder()
                        .id(mockEntities.getId())
                        .description(mockEntities.getDescription())
                        .title(mockEntities.getTitle())
                        .spec(mockEntities.getSpec())
                        .swagger(mockEntities.getSwagger())
                        .updatedDate(new Date())
                        .users(userMocksEntitiesList)
                        .updatedBy(MockCreatorEntities.builder().userId(rootUser.getId()).username(rootUser.getUsername()).build())
                        .build();
                this.mockRepository.save(updateMockenties);
            });
        }

        LOGGER.info("user root already created will pass the process");
    }
}
