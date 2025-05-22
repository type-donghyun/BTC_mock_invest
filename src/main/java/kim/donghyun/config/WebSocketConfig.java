package kim.donghyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // ⭐ STOMP 메시지 브로커 사용
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 연결할 WebSocket endpoint
        registry.addEndpoint("/ws/order")
                .setAllowedOriginPatterns("*") // CORS 허용
                .withSockJS(); // ⭐ SockJS fallback
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 서버 → 클라이언트 브로드캐스트 경로
        registry.enableSimpleBroker("/topic");

        // 클라이언트 → 서버 요청 경로 prefix (ex: /app/sendOrder)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
