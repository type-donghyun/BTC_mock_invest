package kim.donghyun.trade.strategy;

import static kim.donghyun.trade.entity.enums.OrderType.BUY;
import static kim.donghyun.trade.entity.enums.OrderType.SELL;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import kim.donghyun.trade.dto.OrderExecutedMessage;
import kim.donghyun.trade.dto.OrderRequestDto;
import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.entity.Wallet;
import kim.donghyun.trade.entity.enums.OrderStatus;
import kim.donghyun.trade.repository.OrderRepository;
import kim.donghyun.trade.service.PositionService;
import kim.donghyun.trade.service.WalletService;
import kim.donghyun.trade.websocket.OrderPushService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MarketOrderStrategy implements OrderStrategy {

    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final OrderPushService orderPushService;
    private final PositionService positionService;
    private final PriceCache priceCache;

    @Override
    public Order processOrder(OrderRequestDto dto) {
        Wallet wallet = walletService.getWallet(dto.getUserId());

        double price = priceCache.getCurrentPrice();
        if (price <= 0) {
            throw new IllegalStateException("⚠ 실시간 시세(priceCache)가 0 이하입니다.");
        }

        double qty = dto.getQuantity();
        double leverage = dto.getLeverage();

        // 지갑 처리
        if (dto.getOrderType() == BUY) {
            walletService.handleBuy(wallet, price, qty, leverage);
        } else if (dto.getOrderType() == SELL) {
            walletService.handleSell(wallet, price, qty, leverage);
        }

        // 주문 저장
        Order order = Order.builder()
                .userId(dto.getUserId())
                .orderType(dto.getOrderType())
                .orderMode(dto.getOrderMode())
                .tradeMode(dto.getTradeMode()) // 필수
                .price(price)
                .quantity(qty)
                .leverage(leverage)
                .status(OrderStatus.FILLED)
                .createdAt(LocalDateTime.now())
                .executedAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // WebSocket 체결 메시지 생성
        OrderExecutedMessage message = new OrderExecutedMessage(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getOrderType().name(),
                savedOrder.getTradeMode() != null ? savedOrder.getTradeMode().name() : "UNKNOWN",
                savedOrder.getPrice(),
                savedOrder.getQuantity(),
                savedOrder.getExecutedAt().toString(),
                0.0 // 기본 값, 나중에 setPnlPercent로 수정 해도 됌
        );

        // 수익률 계산 후 메시지에 세팅
        positionService.getOpenPosition(savedOrder.getUserId(), savedOrder.getTradeMode())
                .ifPresent(pos -> {
                    double pnl = positionService.calculatePnlPercent(pos, price);
                    message.setPnlPercent(pnl);
                });

        // WebSocket 알림 전송
        orderPushService.sendExecutedMessage(message);

        return savedOrder;
    }
}
