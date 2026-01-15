package xyz.cx233.game.gameimpl.tictactoe.config;

import org.springframework.stereotype.Component;
import xyz.cx233.game.gameimpl.tictactoe.TicTacToeGame;
import xyz.cx233.game.platform.game.GameModuleFactory;
import xyz.cx233.game.platform.game.GameRegistry;
import xyz.cx233.game.platform.game.api.GameModule;

@Component
public class TicTacToeFactory implements GameModuleFactory {

    public TicTacToeFactory(GameRegistry registry) {
        registry.register(this);
    }

    @Override
    public String gameId() {
        return "tictactoe";
    }

    @Override
    public GameModule create() {
        return new TicTacToeGame();
    }
}
