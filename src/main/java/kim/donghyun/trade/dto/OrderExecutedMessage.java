package kim.donghyun.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderExecutedMessage {
	private final String type = "executed"; // ✅ 추가
    private Long orderId;
    private Long userId;
    private String orderType;    // "BUY" or "SELL"
    private String tradeMode;     // SPOT, LONG, SHORT
    private double price;
    private double quantity;
    private String executedAt;   // ISO8601 형태 문자열로 전송
    
    // ✅ 수익률 필드 추가
    private double pnlPercent; // 예: +14.52 (%)
}
