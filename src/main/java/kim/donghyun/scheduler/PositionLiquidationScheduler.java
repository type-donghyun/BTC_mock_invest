package kim.donghyun.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kim.donghyun.trade.entity.Position;
import kim.donghyun.trade.repository.PositionRepository;
import kim.donghyun.trade.service.PositionService;
import kim.donghyun.trade.service.WalletService;
import kim.donghyun.trade.service.OrderPushService; 
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
            if (pnl <= -90.0) { // 강제 청산 조건
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
}

