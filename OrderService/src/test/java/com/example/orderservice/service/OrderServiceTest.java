package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {

    @Test
    void testCreateOrder() {
        // given
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        OrderService orderService = new OrderService(orderRepository);

        Order order = new Order();
        order.setUserId(1L);

        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        Order created = orderService.createOrder(order);

        // then
        assertThat(created.getStatus()).isEqualTo(1);
        assertThat(created.getCreated()).isNotNull();
    }

    @Test
    void testGetOrderById() {
        // given
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        OrderService orderService = new OrderService(orderRepository);

        Order order = new Order();
        order.setId(10L);

        Mockito.when(orderRepository.findById(10L))
                .thenReturn(Optional.of(order));

        // when
        Optional<Order> found = orderService.getOrderById(10L);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(10L);
    }
}
