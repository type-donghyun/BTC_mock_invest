package kim.donghyun.trade.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.trade.dto.OrderRequestDto;
import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.entity.enums.OrderMode;
import kim.donghyun.trade.entity.enums.TradeMode;
import kim.donghyun.trade.repository.OrderRepository;
import kim.donghyun.trade.strategy.LimitOrderStrategy;
import kim.donghyun.trade.strategy.LongMarketOrderStrategy;
import kim.donghyun.trade.strategy.MarketOrderStrategy;
import kim.donghyun.trade.strategy.OrderStrategy;
import kim.donghyun.trade.strategy.ShortMarketOrderStrategy;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MarketOrderStrategy marketOrderStrategy;
    private final LimitOrderStrategy limitOrderStrategy;
    private final OrderRepository orderRepository;
    private final ShortMarketOrderStrategy shortMarketOrderStrategy;
    private final LongMarketOrderStrategy longMarketOrderStrategy;


    private OrderStrategy resolveStrategy(TradeMode tradeMode, OrderMode orderMode) {
        return switch (tradeMode) {
            case SPOT -> resolveSpotStrategy(orderMode);
            case LONG -> resolveLongStrategy(orderMode);
            case SHORT -> resolveShortStrategy(orderMode);
        };
    }

    private OrderStrategy resolveSpotStrategy(OrderMode orderMode) {
        return switch (orderMode) {
            case MARKET -> marketOrderStrategy;
            case LIMIT -> limitOrderStrategy;
        };
    }

    public Order placeOrder(OrderRequestDto dto) {
        TradeMode tradeMode = dto.getTradeMode();
        OrderMode orderMode = dto.getOrderMode();

        OrderStrategy strategy = resolveStrategy(tradeMode, orderMode);
        return strategy.processOrder(dto);
    }


    // 다음은 롱, 숏 전략 추가하면서 구현할 예정
    private OrderStrategy resolveLongStrategy(OrderMode orderMode) {
        return switch (orderMode) {
            case MARKET -> longMarketOrderStrategy;
            case LIMIT -> throw new UnsupportedOperationException("LONG 지정가는 아직 구현되지 않았습니다.");
        };
    }

    private OrderStrategy resolveShortStrategy(OrderMode orderMode) {
        return switch (orderMode) {
            case MARKET -> shortMarketOrderStrategy;
            case LIMIT -> throw new UnsupportedOperationException("SHORT 지정가는 아직 구현되지 않았습니다.");
        };
    }
    
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
