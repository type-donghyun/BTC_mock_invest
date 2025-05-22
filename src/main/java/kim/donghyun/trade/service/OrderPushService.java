package kim.donghyun.trade.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import kim.donghyun.trade.dto.OrderExecutedMessage;
import kim.donghyun.trade.entity.Position;

@Service
public class OrderPushService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public OrderPushService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendLiquidationMessage(Position pos, double price, double pnlPercent) {
        OrderExecutedMessage msg = new OrderExecutedMessage(
            -1L, // orderId 없음
            pos.getUserId(),
            "LIQUIDATED",
            pos.getTradeMode().name(),
            price,
            pos.getQuantity(),
            LocalDateTime.now().toString(),
            pnlPercent
        );

        // 👉 /topic/orders/1 같은 경로로 전송됨
        messagingTemplate.convertAndSend("/topic/orders/" + pos.getUserId(), msg);
    }
}
