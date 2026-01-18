package xyz.cx233.game.platform.game;

import java.util.Map;

public interface GameBroadcaster {

    void broadcast(Map<String, Object> gameState);

    void sendTo(String userId, Object msg);

    void sendSnapshot(String gameId, Object snapshot);
}
