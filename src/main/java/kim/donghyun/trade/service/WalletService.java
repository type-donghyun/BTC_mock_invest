package kim.donghyun.trade.service;

import org.springframework.stereotype.Service;

import kim.donghyun.trade.entity.Position;
import kim.donghyun.trade.entity.Wallet;
import kim.donghyun.trade.entity.enums.TradeMode;
import kim.donghyun.trade.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private static final double MIN_DEPOSIT_AMOUNT = 100.0;
    private static final double MAX_DEPOSIT_AMOUNT = 1_000_000.0;
//    private static final double DEFAULT_DEPOSIT_LIMIT = 1000.0;

    private static final double MIN_LEVERAGE = 1.0;
    private static final double MAX_LEVERAGE = 100.0;

    public Wallet getWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
    }

    public Wallet createDefaultWallet(Long userId) {
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .usdtBalance(0.0)
                .btcBalance(0.0)
                .leverage(MIN_LEVERAGE)
                .totalPnL(0.0)
                .build();

        return walletRepository.save(wallet);
    }

    public void updateWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

    public void handleBuy(Wallet wallet, double price, double quantity, double leverage) {
        validateLeverage(leverage);
        double totalCost = price * quantity / leverage;

        if (wallet.getUsdtBalance() < totalCost) {
            throw new RuntimeException("잔고 부족");
        }

        wallet.setUsdtBalance(wallet.getUsdtBalance() - totalCost);
        wallet.setBtcBalance(wallet.getBtcBalance() + quantity);
        walletRepository.save(wallet);
    }

    public void handleSell(Wallet wallet, double price, double quantity, double leverage) {
        validateLeverage(leverage);
        if (wallet.getBtcBalance() < quantity) {
            throw new RuntimeException("BTC 부족");
        }

        double proceeds = price * quantity * leverage;

        wallet.setBtcBalance(wallet.getBtcBalance() - quantity);
        wallet.setUsdtBalance(wallet.getUsdtBalance() + proceeds);
        walletRepository.save(wallet);
    }

    public void handleShortSell(Wallet wallet, double price, double quantity, double leverage) {
        validateLeverage(leverage);
        double margin = price * quantity / leverage;

        if (wallet.getUsdtBalance() < margin) {
            throw new RuntimeException("잔고 부족 - 숏 진입 불가");
        }

        wallet.setUsdtBalance(wallet.getUsdtBalance() - margin);
        wallet.setTotalPnL(wallet.getTotalPnL()); // 숏 포지션 진입 → 손익 계산 필요 시 추후 적용
        walletRepository.save(wallet);
    }

    public void handleShortCover(Wallet wallet, double price, double quantity, double leverage) {
        validateLeverage(leverage);
        double marginReturn = price * quantity / leverage;

        wallet.setUsdtBalance(wallet.getUsdtBalance() + marginReturn);
        wallet.setTotalPnL(wallet.getTotalPnL()); // 숏 청산 → 손익 계산 필요 시 추후 적용
        walletRepository.save(wallet);
    }

    public void deposit(Long userId, double amount) {
        if (amount < MIN_DEPOSIT_AMOUNT || amount > MAX_DEPOSIT_AMOUNT) {
            throw new IllegalArgumentException("충전 금액은 최소 100$, 최대 1,000,000$까지 가능합니다.");
        }

        Wallet wallet = getWallet(userId);
        wallet.setUsdtBalance(wallet.getUsdtBalance() + amount);
        walletRepository.save(wallet);
    }

    public void resetWallet(Long userId) {
        Wallet wallet = getWallet(userId);
        wallet.setUsdtBalance(0.0);
        wallet.setBtcBalance(0.0);
        wallet.setTotalPnL(0.0);
        wallet.setLeverage(MIN_LEVERAGE);
        walletRepository.save(wallet);
    }

    private void validateLeverage(double leverage) {
        if (leverage < MIN_LEVERAGE || leverage > MAX_LEVERAGE) {
            throw new IllegalArgumentException("레버리지는 1배에서 100배 사이로 설정해야 합니다.");
        }
    }
    
    private double calculatePnL(Position pos, double currentPrice) {
    	double entry = pos.getEntryPrice();
    	double leverage = pos.getLeverage();
    	double qty = pos.getQuantity();
    	
    	if (pos.getTradeMode() == TradeMode.LONG) {
    		return (currentPrice - entry) * qty * leverage;
    	} else if (pos.getTradeMode() == TradeMode.SHORT) {
    		return (entry - currentPrice) * qty * leverage;
    	} else {
    		return 0.0;
    	}
    }
    
    // 강제청산
    public void liquidatePosition(Position pos, double price) {
        Wallet wallet = walletRepository.findByUserId(pos.getUserId())
            .orElseThrow(() -> new RuntimeException("지갑 없음"));

        double pnl = calculatePnL(pos, price);
        wallet.setUsdtBalance(wallet.getUsdtBalance() + pnl); // 손익 반영
        walletRepository.save(wallet);
    }
}