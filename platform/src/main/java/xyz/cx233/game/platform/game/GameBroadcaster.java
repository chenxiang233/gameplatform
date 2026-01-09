package xyz.cx233.game.platform.game;

public interface GameBroadcaster {

    void broadcast(Object gameState);

    void sendTo(String userId, Object msg);
}
