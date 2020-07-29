package com.github.dekaulitz.mockyup;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
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

import java.util.Arrays;
import java.util.Date;

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
    //injecting user for the first time
    public void run(String... args) throws Exception {
        // injecting user root
        UserEntities userEntities = this.userRepository.findFirstByUsername("root");
        if (userEntities == null) {
            LOGGER.info("user not found will create new user root");
            UserEntities rootUser = new UserEntities();
            //default username
            rootUser.setUsername("root");
            //default password for root is root
            rootUser.setPassword(Hash.hashing("root"));
            rootUser.setUpdatedDate(new Date());
            //grant all accesss
            rootUser.setAccessList(Arrays.asList(Role.MOCKS_READ_WRITE.name(), Role.USERS_READ_WRITE.name()));
            this.userRepository.save(rootUser);
            //will check if there is old swagger that available
            this.mockRepository.injectRootIntoAllMocks(rootUser);
        }
        LOGGER.info("user root already created will pass the process");
    }
}
