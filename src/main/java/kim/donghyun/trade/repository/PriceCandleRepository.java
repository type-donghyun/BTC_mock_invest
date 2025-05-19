package kim.donghyun.trade.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kim.donghyun.trade.entity.PriceCandle;

@Repository
public interface PriceCandleRepository extends JpaRepository<PriceCandle, Long> {

	// 1분 조회용
    List<PriceCandle> findByCandleTimeBetween(LocalDateTime from, LocalDateTime to);
    
    @Query("SELECT c FROM PriceCandle c ORDER BY c.candleTime DESC")
    List<PriceCandle> findRecentCandles(Pageable pageable);
    
    // 30일 지난 1분봉 삭제용 (데이터 정리)
    void deleteByCandleTimeBefore(LocalDateTime threshold);
}
