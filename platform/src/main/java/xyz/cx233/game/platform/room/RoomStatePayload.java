package xyz.cx233.game.platform.room;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoomStatePayload {

    private String roomId;
    private String ownerId;
    private RoomState state;

    private List<PlayerState> players;
}

