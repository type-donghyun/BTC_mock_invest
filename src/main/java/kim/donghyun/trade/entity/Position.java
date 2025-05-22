package kim.donghyun.trade.entity;

import jakarta.persistence.*;
import kim.donghyun.trade.entity.enums.TradeMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private TradeMode tradeMode; // SPOT은 저장 X, LONG / SHORT만 해당

    private double entryPrice;   // 진입가 (가중 평균)
    private double quantity;     // 진입 수량 (BTC 단위)
    private double leverage;     // 레버리지 배율

    private boolean Open;      // 포지션 열려 있는지 여부
}
