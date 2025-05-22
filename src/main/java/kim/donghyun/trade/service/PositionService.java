package kim.donghyun.trade.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kim.donghyun.trade.entity.Position;
import kim.donghyun.trade.entity.PositionHistory;
import kim.donghyun.trade.entity.enums.TradeMode;
import kim.donghyun.trade.repository.PositionHistoryRepository;
import kim.donghyun.trade.repository.PositionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionHistoryRepository positionHistoryRepository;

    /**
     * 진입가 가중 평균 계산
     */
    private double calculateNewEntryPrice(double oldQty, double oldEntryPrice, double newQty, double newEntryPrice) {
        return (oldQty * oldEntryPrice + newQty * newEntryPrice) / (oldQty + newQty);
    }

    /**
     * 롱 포지션 진입 (BUY)
     */
    @Transactional
    public void enterLongPosition(Long userId, double price, double qty, double leverage) {
        Position pos = positionRepository
            .findByUserIdAndTradeModeAndIsOpenTrue(userId, TradeMode.LONG)
            .orElseGet(() -> {
                Position newPos = new Position();
                newPos.setUserId(userId);
                newPos.setTradeMode(TradeMode.LONG);
                newPos.setLeverage(leverage);
                newPos.setOpen(true);
             // ✅ 진입 시각 세팅
                newPos.setCreatedAt(LocalDateTime.now());
                return newPos;
            });

        double newEntryPrice = pos.getQuantity() > 0
            ? calculateNewEntryPrice(pos.getQuantity(), pos.getEntryPrice(), qty, price)
            : price;

        pos.setEntryPrice(newEntryPrice);
        pos.setQuantity(pos.getQuantity() + qty);
        positionRepository.save(pos);
    }

    /**
     * 롱 포지션 청산 (SELL)
     */
    @Transactional
    public void exitLongPosition(Long userId, double qtyToClose) {
        Position pos = positionRepository
            .findByUserIdAndTradeModeAndIsOpenTrue(userId, TradeMode.LONG)
            .orElseThrow(() -> new IllegalStateException("열린 롱 포지션이 없습니다."));

        double remainingQty = pos.getQuantity() - qtyToClose;
        if (remainingQty <= 0) {
            pos.setQuantity(0);
            pos.setOpen(false);
        } else {
            pos.setQuantity(remainingQty);
        }

        positionRepository.save(pos);
    }

    /**
     * 숏 포지션 진입 (SELL)
     */
    @Transactional
    public void enterShortPosition(Long userId, double price, double qty, double leverage) {
        Position pos = positionRepository
            .findByUserIdAndTradeModeAndIsOpenTrue(userId, TradeMode.SHORT)
            .orElseGet(() -> {
                Position newPos = new Position();
                newPos.setUserId(userId);
                newPos.setTradeMode(TradeMode.SHORT);
                newPos.setLeverage(leverage);
                newPos.setOpen(true);
                return newPos;
            });

        double newEntryPrice = pos.getQuantity() > 0
            ? calculateNewEntryPrice(pos.getQuantity(), pos.getEntryPrice(), qty, price)
            : price;

        pos.setEntryPrice(newEntryPrice);
        pos.setQuantity(pos.getQuantity() + qty);
        positionRepository.save(pos);
    }

    /**
     * 숏 포지션 청산 (BUY)
     */
    @Transactional
    public void exitShortPosition(Long userId, double qtyToClose) {
        Position pos = positionRepository
            .findByUserIdAndTradeModeAndIsOpenTrue(userId, TradeMode.SHORT)
            .orElseThrow(() -> new IllegalStateException("열린 숏 포지션이 없습니다."));

        double remainingQty = pos.getQuantity() - qtyToClose;
        if (remainingQty <= 0) {
            pos.setQuantity(0);
            pos.setOpen(false);
        } else {
            pos.setQuantity(remainingQty);
        }

        positionRepository.save(pos);
    }

    /**
     * 포지션 조회
     */
    public Optional<Position> getOpenPosition(Long userId, TradeMode tradeMode) {
        return positionRepository.findByUserIdAndTradeModeAndIsOpenTrue(userId, tradeMode);
    }
    
    /**
     * 현재가 기반 실시간 PnL% 계산
     * LONG: (현재가 - 진입가) / 진입가 × 100 × 레버리지
     * SHORT: (진입가 - 현재가) / 진입가 × 100 × 레버리지
     */
    public double calculatePnlPercent(Position position, double currentPrice) {
        double entry = position.getEntryPrice();
        double leverage = position.getLeverage();
        double pnl;

        if (position.getTradeMode() == TradeMode.LONG) {
            pnl = (currentPrice - entry) / entry * 100 * leverage;
        } else if (position.getTradeMode() == TradeMode.SHORT) {
            pnl = (entry - currentPrice) / entry * 100 * leverage;
        } else {
            pnl = 0.0;
        }

        return Math.round(pnl * 100.0) / 100.0; // 소수점 2자리 반올림
    }
    
    public void savePositionHistory(Position pos, double exitPrice, double pnlPercent) {
        PositionHistory history = PositionHistory.builder()
                .userId(pos.getUserId())
                .tradeMode(pos.getTradeMode())
                .entryPrice(pos.getEntryPrice())
                .exitPrice(exitPrice)
                .quantity(pos.getQuantity())
                .leverage(pos.getLeverage())
                .pnlPercent(pnlPercent)
                .entryAt(pos.getCreatedAt()) // 또는 진입 시각 필드
                .exitAt(LocalDateTime.now())
                .build();

        positionHistoryRepository.save(history);
    }

    // 유지 마진율 계산 함수
    private double getMaintenanceMarginRate(double leverage) {
        if (leverage <= 5) return 0.01;
        else if (leverage <= 10) return 0.03;
        else if (leverage <= 25) return 0.05;
        else if (leverage <= 50) return 0.10;
        else return 0.15; // 100x 이상
    }

}
