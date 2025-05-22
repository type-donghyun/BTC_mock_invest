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
    
//	public OrderExecutedMessage(Long orderId, Long userId, String orderType, String tradeMode, double price,
//			double quantity, String executedAt, double pnlPercent) {
//		this.orderId = orderId;
//		this.userId = userId;
//		this.orderType = orderType;
//		this.tradeMode = tradeMode;
//		this.price = price;
//		this.quantity = quantity;
//		this.executedAt = executedAt;
//		this.pnlPercent = pnlPercent;
//	}
}
