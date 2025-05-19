package kim.donghyun.trade.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.trade.entity.PriceCandle;
import kim.donghyun.trade.entity.PriceCandle15m;
import kim.donghyun.trade.entity.PriceCandle1M;
import kim.donghyun.trade.entity.PriceCandle1d;
import kim.donghyun.trade.entity.PriceCandle1h;
import kim.donghyun.trade.entity.PriceCandle1w;
import kim.donghyun.trade.entity.PriceCandle3h;
import kim.donghyun.trade.repository.PriceCandle15mRepository;
import kim.donghyun.trade.repository.PriceCandle1MRepository;
import kim.donghyun.trade.repository.PriceCandle1dRepository;
import kim.donghyun.trade.repository.PriceCandle1hRepository;
import kim.donghyun.trade.repository.PriceCandle1wRepository;
import kim.donghyun.trade.repository.PriceCandle3hRepository;
import kim.donghyun.trade.repository.PriceCandleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandleAggregationService {

    private final PriceCandleRepository oneMinRepo;
    private final PriceCandle15mRepository candle15mRepo;
    private final PriceCandle1hRepository candle1hRepo;
    private final PriceCandle3hRepository candle3hRepo;
    private final PriceCandle1dRepository candle1dRepo;
    private final PriceCandle1wRepository candle1wRepo;
    private final PriceCandle1MRepository candle1MRepo;

    public void generateCandle(LocalDateTime from, LocalDateTime to, String type) {
        List<PriceCandle> candles = oneMinRepo.findByCandleTimeBetween(from, to);

        if (candles.isEmpty()) return;

        BigDecimal open = candles.get(0).getOpen();
        BigDecimal close = candles.get(candles.size() - 1).getClose();
        BigDecimal high = candles.stream().map(PriceCandle::getHigh).max(BigDecimal::compareTo).orElse(open);
        BigDecimal low = candles.stream().map(PriceCandle::getLow).min(BigDecimal::compareTo).orElse(open);

        switch (type) {
            case "15m" -> candle15mRepo.save(new PriceCandle15m(from, open, high, low, close));
            case "1h" -> candle1hRepo.save(new PriceCandle1h(from, open, high, low, close));
            case "3h" -> candle3hRepo.save(new PriceCandle3h(from, open, high, low, close));
            case "1d" -> candle1dRepo.save(new PriceCandle1d(from, open, high, low, close));
            case "1w" -> candle1wRepo.save(new PriceCandle1w(from, open, high, low, close));
            case "1M" -> candle1MRepo.save(new PriceCandle1M(from, open, high, low, close));
        }
    }
}
