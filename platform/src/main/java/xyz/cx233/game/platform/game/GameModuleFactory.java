package xyz.cx233.game.platform.game;

import xyz.cx233.game.platform.game.api.GameModule;

public interface GameModuleFactory {

    String gameId();

    GameModule create();
}
