package kim.donghyun.trade.controller;

import kim.donghyun.util.PriceCache;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/price")
public class PriceController {

    private final PriceCache priceCache;

    public PriceController(PriceCache priceCache) {
        this.priceCache = priceCache;
    }

    @GetMapping
    public ResponseEntity<BigDecimal> getCurrentPrice() {
        BigDecimal cachedPrice = priceCache.getCachedPrice();
        if (cachedPrice == null) {
            return ResponseEntity.status(503).body(null);
        }
        return ResponseEntity.ok(cachedPrice);
    }
}

// GET http://localhost:8080/api/price 현재 비트코인 시세 1초마다 생성
