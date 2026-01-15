package xyz.cx233.game.platform.game.api;

/**
 * 支持游戏状态快照
 */
public interface SnapshotAwareGame {

    /**
     * 获取当前完整游戏状态
     * 用于重连 / 观战
     */
    Object snapshot();
    void broadcastSnapshot();
}
