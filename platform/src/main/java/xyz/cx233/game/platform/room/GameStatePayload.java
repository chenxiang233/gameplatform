package xyz.cx233.game.platform.room;

import lombok.Data;

import java.util.List;

@Data
public class GameStatePayload extends RoomStatePayload{
    private Object snapshot;
    public GameStatePayload(String roomId, String ownerId, RoomState state, List<PlayerState> players) {
        super(roomId, ownerId, state, players);
    }
    public GameStatePayload(String roomId, String ownerId, RoomState state, List<PlayerState> players, Object snapshot) {
        super(roomId, ownerId, state, players);
        this.snapshot = snapshot;
    }
}
