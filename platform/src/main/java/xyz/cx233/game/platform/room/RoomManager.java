package xyz.cx233.game.platform.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import xyz.cx233.game.platform.game.GameManager;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomManager {

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    public Room createRoom(String roomId, String ownerId) {
        Room room = new Room(roomId, ownerId);
        rooms.put(roomId, room);
        return room;
    }

    public Collection<Room> allRooms() {
        return rooms.values();
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public void removeRoom(String roomId) {
        rooms.remove(roomId);
    }


    public void broadcastRoomState(Room room) throws Exception {

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
        msg.setVersion(room.getVersion());

        String json = mapper.writeValueAsString(msg);

        for (Player p : room.allPlayers()) {
            p.getSession().sendMessage(new TextMessage(json));
        }
    }
}
