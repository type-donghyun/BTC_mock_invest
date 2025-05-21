package kim.donghyun.trade.strategy;

import kim.donghyun.trade.dto.OrderRequestDto;
import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.entity.Wallet;
import kim.donghyun.trade.entity.enums.OrderStatus;
import kim.donghyun.trade.repository.OrderRepository;
import kim.donghyun.trade.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static kim.donghyun.trade.entity.enums.OrderType.*;

@Component
@RequiredArgsConstructor
public class MarketOrderStrategy implements OrderStrategy {

    private final OrderRepository orderRepository;
    private final WalletService walletService;

    @Override
    public Order processOrder(OrderRequestDto dto) {
        Wallet wallet = walletService.getWallet(dto.getUserId());

        double price = dto.getPrice(); // 실시간 시세라고 가정
        double qty = dto.getQuantity();
        double leverage = dto.getLeverage();

        if (dto.getOrderType() == BUY) {
            walletService.handleBuy(wallet, price, qty, leverage);
        } else if (dto.getOrderType() == SELL) {
            walletService.handleSell(wallet, price, qty, leverage);
        }

        Order order = Order.builder()
                .userId(dto.getUserId())
                .orderType(dto.getOrderType())
                .orderMode(dto.getOrderMode())
                .price(price)
                .quantity(qty)
                .leverage(leverage)
                .status(OrderStatus.FILLED)
                .createdAt(LocalDateTime.now())
                .executedAt(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }
}
