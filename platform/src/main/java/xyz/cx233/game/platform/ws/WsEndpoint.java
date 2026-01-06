package xyz.cx233.game.platform.ws;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WsEndpoint extends TextWebSocketHandler {

    private final WsDispatcher dispatcher;
    private final WsSessionManager sessionManager;

    public WsEndpoint(WsDispatcher dispatcher,
                      WsSessionManager sessionManager) {
        this.dispatcher = dispatcher;
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionManager.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) {
        dispatcher.dispatch(message.getPayload(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) {
        sessionManager.remove(session);
    }
}
