package xyz.cx233.game.platform.game;

public interface GameModuleFactory {

    String gameId();

    GameModule create();
}
