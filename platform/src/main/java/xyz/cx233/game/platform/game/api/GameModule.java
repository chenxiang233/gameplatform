package xyz.cx233.game.platform.game.api;

import xyz.cx233.game.platform.game.GameBroadcaster;
import xyz.cx233.game.platform.game.GameContext;

public interface GameModule {

    /**
     * 游戏唯一标识
     */
    String gameId();

    /**
     * 游戏初始化（START_GAME 时调用）
     */
    void onStart(GameContext context, GameBroadcaster broadcaster);

    /**
     * 玩家操作（WS 消息转发）
     */
    void onPlayerAction(String userId, Object action);

    /**
     * 玩家断线
     */
    void onPlayerDisconnect(String userId);

    /**
     * 游戏结束
     */
    void onStop();

    /**
     * 获取游戏广播器
     * @return
     */
    GameBroadcaster getBroadcaster();
}
