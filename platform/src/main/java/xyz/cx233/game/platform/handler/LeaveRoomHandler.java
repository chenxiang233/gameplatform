package xyz.cx233.game.platform.handler;

import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.room.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


@Component
public class LeaveRoomHandler implements WsHandler {

    private final RoomManager roomManager;

    public LeaveRoomHandler(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void handle(WsMessage message, WebSocketSession session)
            throws Exception {

        String userId = message.getUserId();
        String roomId = message.getRoomId();

        Room room = roomManager.getRoom(roomId);
        if (room == null) return;

        room.removePlayer(userId);

        if (room.allPlayers().isEmpty()) {
            roomManager.removeRoom(roomId);
            return;
        }

        roomManager.broadcastRoomState(room);
    }
}
