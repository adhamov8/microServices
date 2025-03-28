package com.example.authservice.repository;

import com.example.authservice.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByToken(String token);
}
