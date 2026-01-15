package xyz.cx233.game.gameimpl.tictactoe;

public class TicTacToeSnapshot {

    public String[][] board;
    public String currentPlayer;
    public String winner;

    public TicTacToeSnapshot(
            String[][] board,
            String currentPlayer,
            String winner
    ) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
    }
}
