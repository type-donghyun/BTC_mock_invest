package kim.donghyun.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.trade.entity.PriceCandle;
import kim.donghyun.trade.repository.PriceCandleRepository;
import kim.donghyun.trade.service.CandleAggregationService;
import kim.donghyun.util.CandleAggregator;

@Component
public class CandleScheduler {

    private final CandleAggregator candleAggregator;
    private final PriceCandleRepository candleRepository;
    private final CandleAggregationService candleAggregationService;

    public CandleScheduler(CandleAggregator candleAggregator, PriceCandleRepository candleRepository, 
    		CandleAggregationService candleAggregationService) {
        this.candleAggregator = candleAggregator;
        this.candleRepository = candleRepository;
        this.candleAggregationService = candleAggregationService;
    }

    // 매 분 0초마다 실행 (1분봉)
    @Scheduled(cron = "0 * * * * *")
    public void saveCandle() {
    	LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        PriceCandle candle = candleAggregator.completeCandle();
        if (candle != null) {
            candleRepository.save(candle);
            System.out.println("🕒 1분봉 저장됨: " + candle.getClose());
        }
    }
    
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void deleteOld1mCandles() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        candleRepository.deleteByCandleTimeBefore(threshold);
        System.out.println("🧹 30일 지난 1분봉 삭제 완료");
    }
    
    // 15분봉 생성
    @Scheduled(cron = "0 */15 * * * *")
    public void generate15mCandle() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        candleAggregationService.generateCandle(now.minusMinutes(15), now, "15m");
    }

    // 1시간봉 생성
    @Scheduled(cron = "0 0 * * * *")
    public void generate1hCandle() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        candleAggregationService.generateCandle(now.minusHours(1), now, "1h");
    }
    
    // 3시간봉
    @Scheduled(cron = "0 0 */3 * * *")
    public void generate3hCandle() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        candleAggregationService.generateCandle(now.minusHours(3), now, "3h");
    }

    // 1일봉
    @Scheduled(cron = "0 0 0 * * *")
    public void generate1dCandle() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        candleAggregationService.generateCandle(now.minusDays(1), now, "1d");
    }

    // 1주봉
    @Scheduled(cron = "0 0 0 * * MON")  // 매주 월요일 00시
    public void generate1wCandle() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        candleAggregationService.generateCandle(now.minusWeeks(1), now, "1w");
    }

    // 1개월봉
    @Scheduled(cron = "0 0 0 1 * *")  // 매달 1일 00시
    public void generate1mCandle() {
        LocalDateTime now = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        candleAggregationService.generateCandle(now.minusMonths(1), now, "1M");
    }
}
