package kim.donghyun.trade.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kim.donghyun.trade.entity.PositionHistory;
import kim.donghyun.trade.service.PositionHistoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PositionHistoryController {

    private final PositionHistoryService positionHistoryService;

    @GetMapping("/api/positions/history")
    public List<PositionHistory> getPositionHistory(@RequestParam Long userId) {
        return positionHistoryService.getHistoryByUserId(userId);
    }
}
