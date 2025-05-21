package kim.donghyun.trade.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Double usdtBalance;   // 보유 USDT
    private Double btcBalance;    // 보유 BTC

    private Double leverage;      // 현재 설정된 레버리지 (기본 1.0x)

    private Double totalPnL;      // 누적 손익 (optional)
}
