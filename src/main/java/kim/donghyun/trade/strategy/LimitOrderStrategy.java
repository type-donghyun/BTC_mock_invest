package kim.donghyun.trade.strategy;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import kim.donghyun.trade.dto.OrderRequestDto;
import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.entity.enums.OrderStatus;
import kim.donghyun.trade.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LimitOrderStrategy implements OrderStrategy {

    private final OrderRepository orderRepository;

    @Override
    public Order processOrder(OrderRequestDto dto) {
        Order order = Order.builder()
                .userId(dto.getUserId())
                .orderType(dto.getOrderType())
                .orderMode(dto.getOrderMode())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .leverage(dto.getLeverage())
                .status(OrderStatus.OPEN) // 대기 중
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }
}
