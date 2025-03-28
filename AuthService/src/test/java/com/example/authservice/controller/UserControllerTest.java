package com.example.authservice.controller;

import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST /api/register - успешная регистрация")
    void testRegisterSuccess() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(null);

        User user = new User();
        user.setNickname("test");
        user.setEmail("test@example.com");

        Mockito.when(userService.registerUser("test","test@example.com","password"))
                .thenReturn(user);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname":"test",
                                  "email":"test@example.com",
                                  "password":"password"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    @DisplayName("POST /api/register - email уже используется")
    void testRegisterConflict() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(existingUser);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname":"test",
                                  "email":"test@example.com",
                                  "password":"password"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email is already in use"));
    }
}
