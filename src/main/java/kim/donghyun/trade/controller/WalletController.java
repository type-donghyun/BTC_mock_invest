package kim.donghyun.trade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kim.donghyun.trade.service.WalletService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long userId, @RequestParam double amount) {
        walletService.deposit(userId, amount);
        return ResponseEntity.ok("충전 완료: " + amount + " USDT");
    }
    // 사용 예 : POST /api/wallets/deposit?userId=1&amount=5000
    //→ "충전 완료: 5000 USDT"
    
    @PostMapping("/reset")
    public ResponseEntity<String> resetWallet(@RequestParam Long userId) {
        walletService.resetWallet(userId);
        return ResponseEntity.ok("지갑이 초기화되었습니다.");
    }
    // 사용 예 : POST /api/wallets/reset?userId=1
    // → "지갑이 초기화되었습니다." -> 레버리지, 지갑, 수익률 초기화
}

