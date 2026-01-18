package xyz.cx233.game.platform.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;
import xyz.cx233.game.platform.room.*;

@Component
public class ReadyHandler implements WsHandler {

    private final RoomManager roomManager;

    public ReadyHandler(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void handle(WsMessage message, WebSocketSession session)
            throws Exception {

        String userId = message.getUserId();
        String roomId = message.getRoomId();
        boolean ready = (boolean) message.getPayload();

        Room room = roomManager.getRoom(roomId);
        if (room == null || room.getState() != RoomState.WAITING) return;

        room.setReady(userId, ready);
        room.change();
        roomManager.broadcastRoomState(room);
    }

}
