package kim.donghyun.trade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_candle_1d")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceCandle1d {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime candleTime;

    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;

    // 생성자 단축용
    public PriceCandle1d(LocalDateTime candleTime, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        this.candleTime = candleTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }
}
