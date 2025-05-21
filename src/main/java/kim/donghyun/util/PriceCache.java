// 바이낸스 api 비트코인 시세 캐싱

package kim.donghyun.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class PriceCache {

    private BigDecimal cachedPrice = null;
    private volatile double currentPrice = 0.0;

    public void updatePrice(BigDecimal price) {
        this.cachedPrice = price;
    }

    public BigDecimal getCachedPrice() {
        return this.cachedPrice;
    }
   
    public double getCurrentPrice() {
        return currentPrice;
    }
    public void updatePrice(double price) {
        this.currentPrice = price;
    }
}
