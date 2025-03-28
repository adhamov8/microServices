package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.Station;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.StationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private StationService stationService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @DisplayName("GET /api/orders/{id} - успешное получение заказа при валидном токене")
    void testGetOrderByIdSuccess() throws Exception {
        // имитируем успешную проверку токена
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Boolean.class)))
                .thenReturn(new org.springframework.http.ResponseEntity<>(true, org.springframework.http.HttpStatus.OK));

        // имитируем заказ
        Order order = new Order();
        order.setId(1L);
        order.setUserId(100L);
        order.setCreated(LocalDateTime.now());
        Mockito.when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/orders/{id} - 401 без заголовка Authorization")
    void testGetOrderByIdNoAuthHeader() throws Exception {
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isUnauthorized());
    }
}
