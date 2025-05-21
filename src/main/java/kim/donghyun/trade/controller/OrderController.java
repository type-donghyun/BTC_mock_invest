package kim.donghyun.trade.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kim.donghyun.trade.dto.OrderRequestDto;
import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequestDto requestDto) {
        Order newOrder = orderService.placeOrder(requestDto);
        return ResponseEntity.ok(newOrder);
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@RequestParam Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    
}
