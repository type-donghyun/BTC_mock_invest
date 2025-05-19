package kim.donghyun.trade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class CandleResponseDto {
    private LocalDateTime time;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
}
