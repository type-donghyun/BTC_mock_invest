package kim.donghyun.trade.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kim.donghyun.trade.entity.enums.OrderMode;
import kim.donghyun.trade.entity.enums.OrderStatus;
import kim.donghyun.trade.entity.enums.OrderType;
import kim.donghyun.trade.entity.enums.TradeMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderType orderType; // BUY or SELL

    @Enumerated(EnumType.STRING)
    private OrderMode orderMode; // MARKET or LIMIT

    private Double price;    // 지정가일 때만 사용
    private Double quantity; // BTC 수량

    private Double leverage; // 1.0 ~ n 배

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime executedAt; // 체결 시각 (nullable)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeMode tradeMode;
}
