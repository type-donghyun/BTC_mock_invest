package kim.donghyun.trade.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.trade.dto.OrderRequestDto;
import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.entity.enums.OrderMode;
import kim.donghyun.trade.repository.OrderRepository;
import kim.donghyun.trade.strategy.LimitOrderStrategy;
import kim.donghyun.trade.strategy.MarketOrderStrategy;
import kim.donghyun.trade.strategy.OrderStrategy;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MarketOrderStrategy marketOrderStrategy;
    private final LimitOrderStrategy limitOrderStrategy;
    private final OrderRepository orderRepository;


    public Order placeOrder(OrderRequestDto dto) {
        OrderStrategy strategy = resolveStrategy(dto.getOrderMode());
        return strategy.processOrder(dto);
    }

    private OrderStrategy resolveStrategy(OrderMode orderMode) {
        return switch (orderMode) {
            case MARKET -> marketOrderStrategy;
            case LIMIT -> limitOrderStrategy;
        };
    }
    
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
