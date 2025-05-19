package kim.donghyun.trade.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kim.donghyun.trade.dto.CandleResponseDto;
import kim.donghyun.trade.service.ChartService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chart")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @GetMapping
    public List<CandleResponseDto> getChart(
            @RequestParam(defaultValue = "1m") String interval,
            @RequestParam(defaultValue = "60") int limit
    ) {
        return chartService.getCandles(interval, limit);
    }
}
