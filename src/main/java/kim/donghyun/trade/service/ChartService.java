package kim.donghyun.trade.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import kim.donghyun.trade.dto.CandleResponseDto;
import kim.donghyun.trade.entity.PriceCandle;
import kim.donghyun.trade.repository.PriceCandleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final PriceCandleRepository candleRepository;


    public List<CandleResponseDto> getCandles(String interval, int limit) {
    	List<PriceCandle> candles = candleRepository.findRecentCandles(PageRequest.of(0, limit));
    	return candles.stream()
    			.map(c -> CandleResponseDto.builder()
    					.time(c.getCandleTime())
    					.open(c.getOpen())
    					.high(c.getHigh())
    					.low(c.getLow())
    					.close(c.getClose())
    					.build())
    			.collect(Collectors.toList());
}
}
