package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final StationService stationService;
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    @Autowired
    public OrderController(OrderService orderService, StationService stationService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.stationService = stationService;
        this.restTemplate = restTemplate;
        this.authServiceUrl = System.getenv("AUTH_SERVICE_URL");
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header is missing or invalid");
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        System.out.println("Token: " + token);
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(authServiceUrl + "?token=" + token, Boolean.class);
            System.out.println("Token validation response: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody())) {
                if (stationService.getStationById(order.getFromStation().getId()) == null ||
                        stationService.getStationById(order.getToStation().getId()) == null) {
                    return ResponseEntity.badRequest().build();
                }

                Order createdOrder = orderService.createOrder(order);
                return ResponseEntity.ok(createdOrder);
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header is missing or invalid");
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        System.out.println("Token: " + token);
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(authServiceUrl + "?token=" + token, Boolean.class);
            System.out.println("Token validation response: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody())) {
                return orderService.getOrderById(id)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
