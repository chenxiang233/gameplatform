package xyz.cx233.game.platform.game.api;

public interface TurnBasedGame {

    String currentPlayer();

    boolean canAct(String userId);

    void applyAction(String userId, Object action);
}
