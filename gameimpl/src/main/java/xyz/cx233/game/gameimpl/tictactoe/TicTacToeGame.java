package xyz.cx233.game.gameimpl.tictactoe;
import xyz.cx233.game.platform.game.GameContext;
import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.game.api.SnapshotAwareGame;
import xyz.cx233.game.platform.game.api.TurnBasedGame;
import xyz.cx233.game.platform.game.GameBroadcaster;

import java.util.List;
import java.util.Map;

public class TicTacToeGame
        implements GameModule, TurnBasedGame, SnapshotAwareGame {

    private GameBroadcaster broadcaster;
    private List<String> players;

    private final String[][] board = new String[3][3];
    private int turnIndex = 0;
    private String winner;

    // ===== GameModule =====

    @Override
    public String gameId() {
        return "tictactoe";
    }

    @Override
    public void onStart(GameContext context, GameBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
        this.players = context.getPlayerIds();

        // 初始快照
        broadcastSnapshot();
    }

    @Override
    public void onPlayerAction(String userId, Object action) {
        if(canAct(userId)){
            applyAction(userId, action);
        }
    }

    @Override
    public void onPlayerDisconnect(String userId) {
        // 简单处理：不结束游戏
        // 可扩展为超时判负
    }

    @Override
    public void onStop() {
        // 资源清理（如有）
    }

    @Override
    public GameBroadcaster getBroadcaster() {
        return broadcaster;
    }

    // ===== TurnBasedGame =====

    @Override
    public void applyAction(String userId, Object action) {
        Map<?, ?> map = (Map<?, ?>) action;
        int x = (int) map.get("x");
        int y = (int) map.get("y");

        if (board[x][y] != null) return;

        board[x][y] = userId;

        if (checkWin(userId)) {
            winner = userId;
        } else {
            turnIndex = 1 - turnIndex;
        }
        broadcastSnapshot();
    }

    // ===== SnapshotAwareGame =====

    @Override
    public Object snapshot() {
        if(winner!=null) {
            return Map.of(
                    "board", board,
                    "currentPlayer", currentPlayer(),
                    "winner", winner
            );
        }
        return Map.of(
                "board", board,
                "currentPlayer", currentPlayer()
        );
    }

    // ===== internal =====

    public String currentPlayer() {
        return players.get(Math.min(turnIndex, players.size()-1));
    }

    @Override
    public boolean canAct(String userId) {
       return winner == null && currentPlayer().equals(userId);
    }

    @Override
    public void broadcastSnapshot() {
        broadcaster.broadcast(Map.of(
                "type", "GAME_SNAPSHOT",
                "payload", Map.of(
                        "gameType", gameId(),
                        "state", snapshot()
                )
        ));
    }

    private boolean checkWin(String userId) {
        for (int i = 0; i < 3; i++) {
            if (userId.equals(board[i][0]) &&
                    userId.equals(board[i][1]) &&
                    userId.equals(board[i][2])) return true;

            if (userId.equals(board[0][i]) &&
                    userId.equals(board[1][i]) &&
                    userId.equals(board[2][i])) return true;
        }

        return userId.equals(board[0][0]) &&
                userId.equals(board[1][1]) &&
                userId.equals(board[2][2]);
    }
}

