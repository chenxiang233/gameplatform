package xyz.cx233.game.platform.game;

import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.game.api.SnapshotAwareGame;
import xyz.cx233.game.platform.room.Room;
import lombok.Getter;

@Getter
public class GameRuntime {

    private final Room room;
    private final GameModule game;

    public GameRuntime(Room room, GameModule game) {
        this.room = room;
        this.game = game;
    }

}
