// PriceFetcher가 시세를 가져올 때마다 CandleAggregator.update() 호출해줘야 함

package kim.donghyun.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

import org.springframework.stereotype.Component;

import kim.donghyun.trade.entity.PriceCandle;

@Component
public class CandleAggregator {

    private BigDecimal open, high, low, close;
    private LocalDateTime currentCandleTime;

    public void update(BigDecimal price) {
        if (open == null) {
            open = high = low = close = price;
            currentCandleTime = LocalDateTime.now().withSecond(0).withNano(0);
        } else {
            if (price.compareTo(high) > 0) high = price;
            if (price.compareTo(low) < 0) low = price;
            close = price;
        }
    }

    public PriceCandle completeCandle() {
        if (open == null) return null; // 데이터 없음

        PriceCandle candle = PriceCandle.builder()
            .open(open)
            .high(high)
            .low(low)
            .close(close)
            .candleTime(currentCandleTime)
            .build();

        // 초기화
        open = high = low = close = null;
        return candle;
    }
}
