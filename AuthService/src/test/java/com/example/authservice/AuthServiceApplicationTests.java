package com.example.authservice;

import com.example.authservice.controller.AuthController;
import com.example.authservice.model.LoginRequest;
import com.example.authservice.model.Session;
import com.example.authservice.model.User;
import com.example.authservice.service.SessionService;
import com.example.authservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private SessionService sessionService;

	@MockBean
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	@DisplayName("POST /api/login - успешный логин")
	void testLoginSuccess() throws Exception {
		// given
		LoginRequest request = new LoginRequest();
		request.setEmail("test@example.com");
		request.setPassword("password");

		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("$2a$10$111...hashedpass"); // bcrypt hash

		// заглушки
		when(userService.findByEmail("test@example.com")).thenReturn(user);
		when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

		Session mockSession = new Session();
		mockSession.setToken("session-token");
		mockSession.setExpires(LocalDateTime.now().plusHours(1));
		when(sessionService.createSession(user)).thenReturn(mockSession);

		// when / then
		mockMvc.perform(post("/api/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                   "email":"test@example.com",
                                   "password":"password"
                                }
                                """))
				.andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString(".")));
	}

	@Test
	@DisplayName("POST /api/login - неудачный логин (неверный пароль)")
	void testLoginFail() throws Exception {
		when(userService.findByEmail("test@example.com")).thenReturn(null); // не нашли пользователя

		mockMvc.perform(post("/api/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                  "email":"test@example.com",
                                  "password":"wrong"
                                }
                                """))
				.andExpect(status().isUnauthorized());
	}
}
