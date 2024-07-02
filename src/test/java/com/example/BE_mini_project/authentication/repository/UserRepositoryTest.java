package com.example.BE_mini_project.authentication.repository;

import com.example.BE_mini_project.authentication.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UsersRepository userRepository;

    @BeforeEach
    void setUp() {
        Users user = new Users();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);
    }

    @Test
    void shouldFindUserByEmail() {
        Optional<Users> user = userRepository.findByEmail("john.doe@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void shouldSaveUser() {
        Users user = new Users();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        userRepository.save(user);

        Optional<Users> foundUser = userRepository.findByEmail("jane.doe@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Jane Doe");
    }
}
