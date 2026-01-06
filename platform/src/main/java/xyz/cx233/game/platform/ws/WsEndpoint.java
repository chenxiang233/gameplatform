package xyz.cx233.game.platform.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import xyz.cx233.game.platform.auth.TokenService;

@Component
public class WsEndpoint extends TextWebSocketHandler {

    private final WsDispatcher dispatcher;
    private final WsSessionManager sessionManager;
    private final TokenService tokenService;

    public WsEndpoint(WsDispatcher dispatcher,
                      WsSessionManager sessionManager,
                      TokenService tokenService) {
        this.dispatcher = dispatcher;
        this.sessionManager = sessionManager;
        this.tokenService = tokenService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {

        String token = getToken(session);
        String userId = tokenService.verify(token);

        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
            return;
        }

        sessionManager.add(session, userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) {

        WsSession wsSession = sessionManager.get(session);
        dispatcher.dispatch(message.getPayload(), wsSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) {
        sessionManager.remove(session);
    }

    private String getToken(WebSocketSession session) {
        return session.getUri().getQuery().replace("token=", "");
    }
}
