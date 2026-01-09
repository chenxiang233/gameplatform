package xyz.cx233.game.platform.game;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameRegistry {

    private final Map<String, GameModuleFactory> factories
            = new ConcurrentHashMap<>();

    public void register(GameModuleFactory factory) {
        factories.put(factory.gameId(), factory);
    }

    public GameModule create(String gameId) {
        GameModuleFactory factory = factories.get(gameId);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown game: " + gameId);
        }
        return factory.create();
    }
}
