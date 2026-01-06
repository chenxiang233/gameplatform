package xyz.cx233.game.platform.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import xyz.cx233.game.platform.ws.WsSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WsSessionManager {

    private final Map<String, WsSession> sessions = new ConcurrentHashMap<>();

    public void add(WebSocketSession raw, String userId) {
        sessions.put(raw.getId(), new WsSession(raw, userId));
    }

    public void remove(WebSocketSession raw) {
        sessions.remove(raw.getId());
    }

    public WsSession get(WebSocketSession raw) {
        return sessions.get(raw.getId());
    }
}
