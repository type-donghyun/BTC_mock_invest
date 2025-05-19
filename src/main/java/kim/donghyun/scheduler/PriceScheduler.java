package kim.donghyun.scheduler;

import java.math.BigDecimal;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.util.CandleAggregator; // ✅ CandleAggregator import
import kim.donghyun.util.PriceCache;
import kim.donghyun.util.PriceFetcher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

    private final PriceFetcher priceFetcher;
    private final PriceCache priceCache;
    private final CandleAggregator candleAggregator; // ✅ 누락된 의존성 추가

    // 생성자 직접 쓴다면 모든 필드를 초기화해야 함
//    public PriceScheduler(PriceFetcher priceFetcher, PriceCache priceCache, CandleAggregator candleAggregator) {
//        this.priceFetcher = priceFetcher;
//        this.priceCache = priceCache;
//        this.candleAggregator = candleAggregator;
//    }

    @Scheduled(fixedRate = 1000)
    public void updatePrice() {
        try {
            BigDecimal currentPrice = priceFetcher.getCurrentBTCPrice();
            priceCache.updatePrice(currentPrice);
            candleAggregator.update(currentPrice); // ✅ 이제 정상 작동
            System.out.println("📈 BTC 가격 갱신됨: " + currentPrice + " USDT");
        } catch (Exception e) {
            System.err.println("❌ 시세 갱신 실패: " + e.getMessage());
        }
    }
}
