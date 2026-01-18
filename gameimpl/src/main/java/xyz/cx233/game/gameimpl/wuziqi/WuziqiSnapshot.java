package xyz.cx233.game.gameimpl.wuziqi;

public class WuziqiSnapshot {

    public int[][] board;
    public String currentPlayer;
    public String winner;

    public WuziqiSnapshot(
            int[][] board,
            String currentPlayer,
            String winner
    ) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
    }
}
