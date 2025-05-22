package kim.donghyun.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import kim.donghyun.trade.dto.OrderExecutedMessage;
import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.entity.enums.OrderStatus;
import kim.donghyun.trade.entity.enums.OrderType;
import kim.donghyun.trade.repository.OrderRepository;
import kim.donghyun.trade.service.PositionService;
import kim.donghyun.trade.service.WalletService;
import kim.donghyun.trade.websocket.OrderPushService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LimitOrderMatcher {

    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final PriceCache priceCache;
    private final PositionService positionService;
    private final OrderPushService orderPushService;

    
    @Scheduled(fixedDelay = 1000) // 5초마다 실행
    @Transactional
    public void checkLimitOrders() {
        double currentPrice = priceCache.getCurrentPrice();
        List<Order> openOrders = orderRepository.findByStatus(OrderStatus.OPEN);

        for (Order order : openOrders) {
            boolean shouldExecute = false;

            if (order.getOrderType() == OrderType.BUY && currentPrice <= order.getPrice()) {
                shouldExecute = true;
            } else if (order.getOrderType() == OrderType.SELL && currentPrice >= order.getPrice()) {
                shouldExecute = true;
            }

            if (shouldExecute) {
                try {
                    if (order.getOrderType() == OrderType.BUY) {
                        walletService.handleBuy(walletService.getWallet(order.getUserId()), currentPrice, order.getQuantity(), order.getLeverage());
                    } else {
                        walletService.handleSell(walletService.getWallet(order.getUserId()), currentPrice, order.getQuantity(), order.getLeverage());
                    }

                    order.setStatus(OrderStatus.FILLED);
                    order.setExecutedAt(LocalDateTime.now());
                    order.setPrice(currentPrice); // 체결된 가격으로 덮어쓰기
                    
                 // 체결 알림 메시지 전송
                    OrderExecutedMessage message = new OrderExecutedMessage(
                        order.getId(),
                        order.getUserId(),
                        order.getOrderType().name(),
                        order.getTradeMode().name(),   // ✅ 여기 추가
                        order.getPrice(),
                        order.getQuantity(),
                        order.getExecutedAt().toString(),
                        0.0
                    );
                    
                    // 수익률 계산 후 메시지에 셋팅
                    positionService.getOpenPosition(order.getUserId(), order.getTradeMode())
                    .ifPresent(pos -> {
                        double pnl = positionService.calculatePnlPercent(pos, currentPrice);
                        message.setPnlPercent(pnl); // 🔥 수익률 포함
                    });
                    
                    orderPushService.sendExecutedMessage(message);


                    log.info("✔ 지정가 주문 체결됨: ID={}, USER={}, TYPE={}, 가격={}", order.getId(), order.getUserId(), order.getOrderType(), currentPrice);
                } catch (RuntimeException e) {
                    log.warn("❌ 잔고 부족으로 주문 실패: ID={}, USER={}, 예외={}", order.getId(), order.getUserId(), e.getMessage());
                }
            }
        }
    }
}
