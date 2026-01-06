package xyz.cx233.game.platform.handler;

import xyz.cx233.game.platform.protocol.WsMessage;
import org.springframework.web.socket.WebSocketSession;

public interface WsHandler {
    void handle(WsMessage message, WebSocketSession session) throws Exception;
}
