package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class OrderProcessingService {

    private final OrderRepository orderRepository;
    private final Random random = new Random();

    @Autowired
    public OrderProcessingService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 500000) // Запуск каждые 5 секунд
    public void processOrders() {
        List<Order> orders = orderRepository.findAllByStatus(1); // Найти все заказы в статусе "check"

        for (Order order : orders) {
            int newStatus = random.nextInt(2) + 2; // Случайным образом выбираем статус "success" (2) или "rejection" (3)
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
    }
}
