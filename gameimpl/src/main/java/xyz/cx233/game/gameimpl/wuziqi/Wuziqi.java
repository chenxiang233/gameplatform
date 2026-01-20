package xyz.cx233.game.gameimpl.wuziqi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.cx233.game.platform.game.GameContext;
import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.game.api.SnapshotAwareGame;
import xyz.cx233.game.platform.game.api.TurnBasedGame;
import xyz.cx233.game.platform.game.GameBroadcaster;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Wuziqi
        implements GameModule, TurnBasedGame, SnapshotAwareGame {

    private static final Logger log = LoggerFactory.getLogger(Wuziqi.class);
    private GameBroadcaster broadcaster;
    private List<String> players;

    private String[][] board;
    private int turnIndex = 0;
    private String winner;
    private final String[] qizi = {"B", "W"};
    private Map<?, ?> gameParams;
    public Wuziqi(){
        this(Map.of(
                "boardSize", List.of(9, 9)
        ));
    }
    /**
     * {
     *     "boardSize": [9, 9]
     * }
     * @param params
     */
    // ===== GameModule =====
    public Wuziqi(Object params){
        this.gameParams = (Map<?, ?>)params;
        init(params);
    }

    private void init(Object params){
        this.board = new String[((List<Integer>) gameParams.get("boardSize")).get(0)][((List<Integer>) gameParams.get("boardSize")).get(1)];
    }



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
        boolean change = false;
        if(type.equals("move")){
            List<Integer> position = (List<Integer>) map.get("position");
            change = move(userId, position);
        }
        if(change){
            broadcastSnapshot();
        }
    }

    private boolean move(String userId, List<Integer> position){
        int x = position.get(0);
        int y = position.get(1);
        if (board[x][y] != null) return false;
        board[x][y] = qizi[turnIndex];
        if (checkWin(userId)) {
            winner = userId;
        } else {
            turnIndex = 1 - turnIndex;
        }
        return true;
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
        Map<String, Object> snapShotMsg = Map.of(
                "type", "GAME_SNAPSHOT",
                "payload", Map.of(
                        "gameType", gameId(),
                        "state", snapshot()
                )
        );
        log.info("发送游戏快照{}", snapShotMsg);
        broadcaster.broadcast(snapShotMsg);
    }

    private boolean checkWin(String userId) {
        return board[0][0] != null;
    }
}

