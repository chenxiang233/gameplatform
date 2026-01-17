package xyz.cx233.game.gameimpl.gogame.config;

import org.springframework.stereotype.Component;
import xyz.cx233.game.gameimpl.tictactoe.TicTacToeGame;
import xyz.cx233.game.platform.game.GameModuleFactory;
import xyz.cx233.game.platform.game.GameRegistry;
import xyz.cx233.game.platform.game.api.GameModule;

@Component
public class GoGameFactory implements GameModuleFactory {

    public GoGameFactory(GameRegistry registry) {
        registry.register(this);
    }

    @Override
    public String gameId() {
        return "gogame";
    }

    @Override
    public GameModule create() {
        return new TicTacToeGame();
    }
}
