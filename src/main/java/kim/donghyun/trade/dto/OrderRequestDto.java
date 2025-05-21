package kim.donghyun.trade.dto;

import kim.donghyun.trade.entity.enums.OrderMode;
import kim.donghyun.trade.entity.enums.OrderType;
import lombok.*;

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
}
