package com.example.authservice.controller;

import com.example.authservice.model.LoginRequest;
import com.example.authservice.model.Session;
import com.example.authservice.model.User;
import com.example.authservice.service.SessionService;
import com.example.authservice.service.UserService;
import com.example.authservice.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService, SessionService sessionService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        Session session = sessionService.createSession(user);
        String jwtToken = JwtUtil.generateToken(session.getToken());
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String token) {
        String sessionToken = JwtUtil.extractToken(token);
        Session session = sessionService.findByToken(sessionToken);
        if (session == null || session.getExpires().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(session.getUser());
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        String sessionToken = JwtUtil.extractToken(token);
        Session session = sessionService.findByToken(sessionToken);
        if (session == null || session.getExpires().isBefore(LocalDateTime.now())) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }
}
