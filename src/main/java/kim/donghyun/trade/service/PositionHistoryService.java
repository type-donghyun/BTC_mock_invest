package kim.donghyun.trade.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.trade.entity.PositionHistory;
import kim.donghyun.trade.repository.PositionHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionHistoryService {

    private final PositionHistoryRepository positionHistoryRepository;

    public List<PositionHistory> getHistoryByUserId(Long userId) {
        return positionHistoryRepository.findByUserIdOrderByExitAtDesc(userId);
    }
}
