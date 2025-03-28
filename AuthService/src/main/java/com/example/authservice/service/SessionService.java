package com.example.authservice.service;

import com.example.authservice.model.Session;
import com.example.authservice.model.User;
import com.example.authservice.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setToken(UUID.randomUUID().toString());
        session.setExpires(LocalDateTime.now().plusHours(1));

        return sessionRepository.save(session);
    }

    public Session findByToken(String token) {
        return sessionRepository.findByToken(token);
    }
}
