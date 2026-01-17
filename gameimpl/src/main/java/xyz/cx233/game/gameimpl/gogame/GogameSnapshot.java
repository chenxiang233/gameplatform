package xyz.cx233.game.gameimpl.gogame;

public class GogameSnapshot {

    public int[][] board;
    public String currentPlayer;
    public String winner;

    public GogameSnapshot(
            int[][] board,
            String currentPlayer,
            String winner
    ) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
    }
}
