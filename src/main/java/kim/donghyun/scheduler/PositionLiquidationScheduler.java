package kim.donghyun.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kim.donghyun.trade.entity.Position;
import kim.donghyun.trade.repository.PositionRepository;
import kim.donghyun.trade.service.OrderPushService;
import kim.donghyun.trade.service.PositionService;
import kim.donghyun.trade.service.WalletService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionLiquidationScheduler {

    private final PositionRepository positionRepository;
    private final PriceCache priceCache;
    private final WalletService walletService;
    private final OrderPushService orderPushService;
    private final PositionService positionService;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void checkForLiquidation() {
        List<Position> openPositions = positionRepository.findByOpenTrue();
        double currentPrice = priceCache.getCurrentPrice(); // 현재 BTC 가격

        for (Position pos : openPositions) {
            double pnl = positionService.calculatePnlPercent(pos, currentPrice);
            double mmRate = getMaintenanceMarginRate(pos.getLeverage()); // 유지 마진 비율
            double liquidationThreshold = -(1 - mmRate) * 100; // 청산 수익률 기준 (예: -95%)

            if (pnl <= liquidationThreshold) {
                // 포지션 종료 처리
                pos.setOpen(false);
                positionRepository.save(pos);

                // 지갑 반영
                walletService.liquidatePosition(pos, currentPrice);

                // WebSocket 메시지 전송
                orderPushService.sendLiquidationMessage(pos, currentPrice, pnl);

                // 포지션 이력 저장
                positionService.savePositionHistory(pos, currentPrice, pnl);
            }
        }
    }

    // ✅ 유지 마진율 계산 함수 (간단한 버전)
    private double getMaintenanceMarginRate(double leverage) {
        if (leverage <= 5) return 0.01;
        else if (leverage <= 10) return 0.03;
        else if (leverage <= 25) return 0.05;
        else if (leverage <= 50) return 0.10;
        else return 0.15; // 100x 이상
    }
}
