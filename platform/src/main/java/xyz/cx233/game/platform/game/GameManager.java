package xyz.cx233.game.platform.game;

import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.room.Room;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameManager {

    private final Map<String, GameRuntime> runningGames = new ConcurrentHashMap<>();
    private final GameRegistry gameRegistry;

    public GameManager(GameRegistry gameRegistry) {
        this.gameRegistry = gameRegistry;
    }

    public void startGame(Room room, String gameId) {
        gameRegistry.gameStartCheck(gameId, room);
        GameModule module = gameRegistry.create(gameId);
        GameContext context = new GameContext(
                room.getRoomId(),
                room.buildPlayerStates()
                        .stream()
                        .map(p -> p.getUserId())
                        .toList()
        );
        module.onStart(context, new WsGameBroadcaster(room));
        runningGames.put(room.getRoomId(),
                new GameRuntime(room, module));
    }

    public GameRuntime getGame(String roomId) {
        return runningGames.get(roomId);
    }

    public void stopGame(String roomId) {
        GameRuntime rt = runningGames.remove(roomId);
        if (rt != null) {
            rt.getGame().onStop();
        }
    }

    public Collection<GameRuntime> getRunningGames() {
        return runningGames.values();
    }
}
