package xyz.cx233.game.platform.ws;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class WsSession {

    private final WebSocketSession session;
    private final String userId;
}
