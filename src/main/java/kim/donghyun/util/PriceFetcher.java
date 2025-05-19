// 바이낸스 BTC 시세 조회 
// 

package kim.donghyun.util;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PriceFetcher {

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getCurrentBTCPrice() {
        String url = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, String> result = response.getBody();

        if (result != null && result.containsKey("price")) {
        	
            return new BigDecimal(result.get("price"));
        } else {
            throw new RuntimeException("BTC 시세를 불러올 수 없습니다.");
        }
    }
}

