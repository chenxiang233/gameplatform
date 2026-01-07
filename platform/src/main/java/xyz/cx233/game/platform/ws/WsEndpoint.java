package xyz.cx233.game.platform.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import xyz.cx233.game.platform.auth.TokenService;
import xyz.cx233.game.platform.room.Room;
import xyz.cx233.game.platform.room.RoomManager;

@Component
public class WsEndpoint extends TextWebSocketHandler {

    private final WsDispatcher dispatcher;
    private final WsSessionManager sessionManager;
    private final TokenService tokenService;
    private final RoomManager roomManager;

    public WsEndpoint(WsDispatcher dispatcher,
                      WsSessionManager sessionManager,
                      TokenService tokenService,
                      RoomManager roomManager) {
        this.dispatcher = dispatcher;
        this.sessionManager = sessionManager;
        this.tokenService = tokenService;
        this.roomManager = roomManager;
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
        String userId = (String) session.getAttributes().get("userId");
        String roomId = (String) session.getAttributes().get("roomId");

        if (userId == null || roomId == null) return;

        Room room = roomManager.getRoom(roomId);
        if (room != null) {
            room.onDisconnect(userId);
            try {
                roomManager.broadcastRoomState(room);
            } catch (Exception e) {
                //log.warn("broadcast failed", e);
            }
        }
    }

    private String getToken(WebSocketSession session) {
        return session.getUri().getQuery().replace("token=", "");
    }
}
