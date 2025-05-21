package kim.donghyun.trade.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderWebSocketHandler implements WebSocketHandler {

    private final OrderPushService orderPushService;

    private final Map<WebSocketSession, Long> sessionUserMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            Long userId = Long.parseLong(session.getUri().getQuery().split("=")[1]);
            sessionUserMap.put(session, userId);
            orderPushService.registerSession(userId, session);
            System.out.println("✅ WebSocket 연결됨 - userId: " + userId);
        } catch (Exception e) {
            System.err.println("❌ WebSocket 연결 실패");
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {}

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = sessionUserMap.remove(session);
        orderPushService.removeSession(userId);
        System.out.println("⛔ WebSocket 해제됨 - userId: " + userId);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("⚠ WebSocket 오류 발생");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
