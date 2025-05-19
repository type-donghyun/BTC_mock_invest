package kim.donghyun.trade.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kim.donghyun.trade.entity.PriceCandle15m;
import kim.donghyun.trade.entity.PriceCandle1M;

@Repository
public interface PriceCandle1MRepository extends JpaRepository<PriceCandle1M, Long> {
    
    // 선택적으로 조회 기능도 추가 가능
    List<PriceCandle1M> findByCandleTimeBetween(LocalDateTime from, LocalDateTime to);
    
    @Query("SELECT c FROM PriceCandle15m c ORDER BY c.candleTime DESC")
    List<PriceCandle15m> findRecentCandles(Pageable pageable);
}
