package kim.donghyun.trade.service;

import kim.donghyun.trade.entity.Wallet;
import kim.donghyun.trade.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
    }

    public Wallet createDefaultWallet(Long userId) {
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .usdtBalance(10000.0) // 초기 자금
                .btcBalance(0.0)
                .leverage(1.0)
                .totalPnL(0.0)
                .build();

        return walletRepository.save(wallet);
    }

    public void updateWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }
    
    public void handleBuy(Wallet wallet, double price, double quantity, double leverage) {
        double totalCost = price * quantity / leverage;

        if (wallet.getUsdtBalance() < totalCost) {
            throw new RuntimeException("잔고 부족");
        }

        wallet.setUsdtBalance(wallet.getUsdtBalance() - totalCost);
        wallet.setBtcBalance(wallet.getBtcBalance() + quantity);

        walletRepository.save(wallet);
    }

    public void handleSell(Wallet wallet, double price, double quantity, double leverage) {
        if (wallet.getBtcBalance() < quantity) {
            throw new RuntimeException("BTC 부족");
        }

        double proceeds = price * quantity * leverage;

        wallet.setBtcBalance(wallet.getBtcBalance() - quantity);
        wallet.setUsdtBalance(wallet.getUsdtBalance() + proceeds);

        walletRepository.save(wallet);
    }

}
