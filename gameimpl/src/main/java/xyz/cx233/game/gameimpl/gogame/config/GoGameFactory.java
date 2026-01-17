package xyz.cx233.game.gameimpl.gogame.config;

import org.springframework.stereotype.Component;
import xyz.cx233.game.gameimpl.gogame.GoGame;
import xyz.cx233.game.gameimpl.tictactoe.TicTacToeGame;
import xyz.cx233.game.platform.game.GameModuleFactory;
import xyz.cx233.game.platform.game.GameRegistry;
import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.room.Room;

@Component
public class GoGameFactory implements GameModuleFactory {

    private static int minPlayers = 2;

    public GoGameFactory(GameRegistry registry) {
        registry.register(this);
    }

    @Override
    public String gameId() {
        return "gogame";
    }

    @Override
    public GameModule create() {
        return new GoGame();
    }
    @Override
    public void gameStartCheck(Room room) {
        allReadyCheck(room);
        minPlayersCheck(room, minPlayers);
    }
}
