package kim.donghyun.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderExecutedMessage {
    private Long orderId;
    private Long userId;
    private String orderType;    // "BUY" or "SELL"
    private double price;
    private double quantity;
    private String executedAt;   // ISO8601 형태 문자열로 전송
}
