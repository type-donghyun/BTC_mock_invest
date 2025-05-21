package kim.donghyun.trade.strategy;

import kim.donghyun.trade.dto.OrderRequestDto;
import kim.donghyun.trade.entity.Order;

public interface OrderStrategy {
    Order processOrder(OrderRequestDto dto);
}
