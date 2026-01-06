package xyz.cx233.game.platform.handler.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;
import xyz.cx233.game.platform.room.Player;
import xyz.cx233.game.platform.room.Room;
import xyz.cx233.game.platform.room.RoomStatePayload;

public interface RoomStateProcess {

    ObjectMapper mapper = new ObjectMapper();

    default void broadcastRoomState(Room room) throws Exception {

        RoomStatePayload payload = new RoomStatePayload(
                room.getRoomId(),
                room.getOwnerId(),
                room.getState(),
                room.buildPlayerStates()
        );

        WsMessage msg = new WsMessage();
        msg.setType(WsType.ROOM_STATE);
        msg.setRoomId(room.getRoomId());
        msg.setPayload(payload);

        String json = mapper.writeValueAsString(msg);

        for (Player p : room.allPlayers()) {
            p.getSession().sendMessage(new TextMessage(json));
        }
    }
}
