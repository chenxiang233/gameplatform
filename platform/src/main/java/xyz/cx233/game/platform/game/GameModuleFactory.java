package xyz.cx233.game.platform.game;

import xyz.cx233.game.platform.exception.room.StartGameException;
import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.room.Room;

public interface GameModuleFactory {

    String gameId();

    GameModule create();
    /**
     * 创建游戏模块
     * @param params 游戏参数
     * @return
     */
    default  GameModule create(Object params){
        return create();
    }

    void gameStartCheck(Room room);

    default void allReadyCheck(Room room) {
        // ③ 全员准备校验
        if (!room.allReadyAndConnected()) {
            throw new StartGameException("NOT_ALL_READY");
        }
    }

    default void minPlayersCheck(Room room, int minPlayers) {
        if (room.allPlayers().size() < minPlayers) {
            throw new StartGameException("至少%s开始游戏", minPlayers);
        }
    }

}
