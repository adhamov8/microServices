package com.example.authservice.service;

import com.example.authservice.model.Session;
import com.example.authservice.model.User;
import com.example.authservice.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SessionServiceTest {

    @Test
    void testCreateSession() {
        // given
        SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
        SessionService sessionService = new SessionService(sessionRepository);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        Mockito.when(sessionRepository.save(Mockito.any(Session.class)))
                .thenAnswer(inv -> inv.getArgument(0)); // возвращаем тот же объект

        // when
        Session created = sessionService.createSession(user);

        // then
        assertThat(created).isNotNull();
        assertThat(created.getUser()).isEqualTo(user);
        assertThat(created.getToken()).isNotEmpty();
        assertThat(created.getExpires()).isAfter(LocalDateTime.now());
    }

    @Test
    void testFindByToken() {
        SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
        SessionService sessionService = new SessionService(sessionRepository);

        Session s = new Session();
        s.setToken("token-123");
        Mockito.when(sessionRepository.findByToken("token-123")).thenReturn(s);

        Session found = sessionService.findByToken("token-123");
        assertThat(found).isNotNull();
        assertThat(found.getToken()).isEqualTo("token-123");
    }
}
