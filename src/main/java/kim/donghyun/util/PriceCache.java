// 바이낸스 api 비트코인 시세 캐싱

package kim.donghyun.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class PriceCache {

    private BigDecimal cachedPrice = null;

    public void updatePrice(BigDecimal price) {
        this.cachedPrice = price;
    }

    public BigDecimal getCachedPrice() {
        return this.cachedPrice;
    }
}
