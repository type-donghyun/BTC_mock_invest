package kim.donghyun.trade.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.donghyun.trade.dto.OrderExecutedMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderPushService {

    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registerSession(Long userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    public void removeSession(Long userId) {
        userSessions.remove(userId);
    }

    public void sendExecutedMessage(OrderExecutedMessage message) {
        WebSocketSession session = userSessions.get(message.getUserId());
        if (session != null && session.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(json));
                System.out.println("📨 체결 메시지 전송: " + json);
            } catch (Exception e) {
                System.err.println("❌ 메시지 전송 실패");
            }
        }
    }
}
