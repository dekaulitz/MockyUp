package com.github.dekaulitz.mockyup;

import com.github.dekaulitz.mockyup.entities.UserEntities;
import com.github.dekaulitz.mockyup.repositories.UserRepository;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class MockyupApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockyupApplication.class);
    @Autowired
    private final UserRepository userRepository;

    public MockyupApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MockyupApplication.class, args);
    }

    @Override
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
        }
        LOGGER.info("user root exist");
    }
}
