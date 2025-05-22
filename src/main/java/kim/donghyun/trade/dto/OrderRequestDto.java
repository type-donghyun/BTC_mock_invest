package kim.donghyun.trade.dto;

import kim.donghyun.trade.entity.enums.OrderMode;
import kim.donghyun.trade.entity.enums.OrderType;
import kim.donghyun.trade.entity.enums.TradeMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    private Long userId;

    private OrderType orderType;   // BUY or SELL
    private OrderMode orderMode;   // MARKET or LIMIT

    private Double price;          // 지정가일 때만 사용
    private Double quantity;       // BTC 수량

    private Double leverage;       // 1.0 이상
    
    private TradeMode tradeMode; // 거래 모드 -> 레버리지 롱, 숏 
}
