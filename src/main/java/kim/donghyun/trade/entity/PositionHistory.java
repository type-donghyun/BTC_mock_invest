package kim.donghyun.trade.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import kim.donghyun.trade.entity.enums.TradeMode;
import lombok.*;

@Entity
@Table(name = "position_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private TradeMode tradeMode; // SPOT / LONG / SHORT

    private double entryPrice;   // 진입가
    private double exitPrice;    // 청산가
    private double quantity;     // 수량
    private double leverage;     // 레버리지
    private double pnlPercent;   // 수익률 (%)

    private LocalDateTime entryAt;
    private LocalDateTime exitAt;
}
