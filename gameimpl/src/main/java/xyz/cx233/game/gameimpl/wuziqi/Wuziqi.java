package xyz.cx233.game.gameimpl.wuziqi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.cx233.game.platform.game.GameContext;
import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.game.api.SnapshotAwareGame;
import xyz.cx233.game.platform.game.api.TurnBasedGame;
import xyz.cx233.game.platform.game.GameBroadcaster;

import java.util.List;
import java.util.Map;


public class Wuziqi
        implements GameModule, TurnBasedGame, SnapshotAwareGame {

    private static final Logger log = LoggerFactory.getLogger(Wuziqi.class);
    private GameBroadcaster broadcaster;
    private List<String> players;

    private final String[][] board = new String[9][9];
    private int turnIndex = 0;
    private String winner;
    private String[] qizi = {"B", "W"};

    // ===== GameModule =====

    @Override
    public String gameId() {
        return "wuziqi";
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
        String type = (String) map.get("type");
        if(type.equals("move")){
            int[] postion = (int[]) map.get("postion");
            move(userId, postion);
        }

    }

    private void move(String userId, int[] postion){
        int x = postion[0];
        int y = postion[1];
        if (board[x][y] != null) return;
        board[x][y] = qizi[turnIndex];
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
        return players.get(turnIndex);
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
        return false;
    }
}

