package xyz.cx233.game.platform.protocol;

public enum WsType {
    PING,
    PONG,
    ERROR,

    JOIN_ROOM,
    LEAVE_ROOM,
    ROOM_STATE,

    QUERY_USER,
    READY,
    START_GAME,
    STOP_GAME,

    GAME_ACTION,   // 玩家操作
    GAME_SYNC,     // 服务端同步
    /**
     * 游戏快照
     * {
     *   "type": "GAME_SNAPSHOT",
     *   "roomId": "room-001",
     *   "payload": {
     *     "game": "tictactoe",
     *     "state": {
     *       "board": [...],
     *       "currentPlayer": "u1001",
     *       "winner": null
     *     }
     *   }
     * }
     */
    GAME_SNAPSHOT,
    ;


    }
