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
        if (checkWin(userId, x, y)) {
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

    private boolean checkWin(String userId, int x, int y) {
        return isWinningMove(board, x, y);
    }

    /**
     * 判断当前落子是否形成五连珠
     * @param board 棋盘数组，null表示空位，'W'表示白子，'B'表示黑子
     * @param x 当前落子的x坐标
     * @param y 当前落子的y坐标
     * @return 是否形成五连珠
     */
    public boolean isWinningMove(String[][] board, int x, int y) {
        // 边界检查
        if (board == null || x < 0 || y < 0 || x >= board.length || y >= board[0].length) {
            return false;
        }

        // 获取当前落子颜色
        String color = board[x][y];
        if (color == null) {
            return false; // 空位不可能赢
        }

        // 定义四个检查方向：水平、垂直、左斜线、右斜线
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        // 检查每个方向
        for (int[] dir : directions) {
            int count = 1; // 从1开始，因为当前点已经算一个

            // 检查正方向
            count += countConsecutive(board, x, y, dir[0], dir[1], color);

            // 检查反方向
            count += countConsecutive(board, x, y, -dir[0], -dir[1], color);

            // 如果任意方向达到5个连续，则获胜
            if (count >= 5) {
                return true;
            }
        }

        return false;
    }

    /**
     * 辅助方法：计算指定方向上的连续同色棋子数量
     * @param board 棋盘数组
     * @param x 起始x坐标
     * @param y 起始y坐标
     * @param dx x方向增量
     * @param dy y方向增量
     * @param color 目标颜色
     * @return 连续同色棋子数量
     */
    private int countConsecutive(String[][] board, int x, int y, int dx, int dy, String color) {
        int count = 0;
        int i = x + dx;
        int j = y + dy;

        // 沿指定方向检查，最多检查4步（因为五子棋只需要5个连续）
        for (int step = 0; step < 4; step++) {
            // 检查边界
            if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
                break;
            }

            // 检查是否为同色棋子
            if (board[i][j] != null && board[i][j].equals(color)) {
                count++;
                i += dx;
                j += dy;
            } else {
                break; // 遇到不同颜色或空位，停止计数
            }
        }

        return count;
    }
}

