package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    @Test
    void testRegisterUser() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);

        UserService userService = new UserService(userRepository, encoder);

        Mockito.when(encoder.encode("pass")).thenReturn("encoded-pass");
        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        User created = userService.registerUser("nick", "nick@example.com", "pass");

        // then
        assertThat(created.getNickname()).isEqualTo("nick");
        assertThat(created.getEmail()).isEqualTo("nick@example.com");
        assertThat(created.getPassword()).isEqualTo("encoded-pass");
        assertThat(created.getCreated()).isNotNull();
    }

    @Test
    void testFindByEmail() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);
        UserService userService = new UserService(userRepository, encoder);

        User user = new User();
        user.setEmail("test@example.com");
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // when
        User found = userService.findByEmail("test@example.com");

        // then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }
}
