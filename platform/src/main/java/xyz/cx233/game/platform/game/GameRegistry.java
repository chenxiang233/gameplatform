package xyz.cx233.game.platform.game;

import org.springframework.stereotype.Component;
import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.room.Room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameRegistry {

    private final Map<String, GameModuleFactory> factories
            = new ConcurrentHashMap<>();

    public void register(GameModuleFactory factory) {
        factories.put(factory.gameId(), factory);
    }


    public GameModule create(String gameId, Object params) {
        return getFactory(gameId).create(params);
    }

    public GameModule create(String gameId) {
        return getFactory(gameId).create();
    }

    public void gameStartCheck(String gameId, Room room){
        getFactory(gameId).gameStartCheck(room);
    }

    private GameModuleFactory getFactory(String gameId){
        GameModuleFactory factory = factories.get(gameId);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown game: " + gameId);
        }
        return factory;
    }
}
