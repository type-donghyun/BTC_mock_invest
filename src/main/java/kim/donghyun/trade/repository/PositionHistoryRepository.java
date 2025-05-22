package kim.donghyun.trade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kim.donghyun.trade.entity.PositionHistory;

@Repository
public interface PositionHistoryRepository extends JpaRepository<PositionHistory, Long> {
    
    // ✅ 유저별 청산 이력 조회
    List<PositionHistory> findByUserIdOrderByExitAtDesc(Long userId);
}
