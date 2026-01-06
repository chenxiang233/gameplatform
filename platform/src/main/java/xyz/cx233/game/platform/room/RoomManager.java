package xyz.cx233.game.platform.room;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomManager {

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    public Room createRoom(String ownerId) {
        String roomId = UUID.randomUUID().toString();
        Room room = new Room(roomId, ownerId);
        rooms.put(roomId, room);
        return room;
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public void removeRoom(String roomId) {
        rooms.remove(roomId);
    }
}
