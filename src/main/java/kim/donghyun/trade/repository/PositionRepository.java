package kim.donghyun.trade.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kim.donghyun.trade.entity.Position;
import kim.donghyun.trade.entity.enums.TradeMode;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByUserIdAndTradeModeAndIsOpenTrue(Long userId, TradeMode tradeMode);
    List<Position> findByOpenTrue();
}
