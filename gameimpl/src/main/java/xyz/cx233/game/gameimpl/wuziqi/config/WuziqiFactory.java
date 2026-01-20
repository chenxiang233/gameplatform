package xyz.cx233.game.gameimpl.wuziqi.config;

import org.springframework.stereotype.Component;
import xyz.cx233.game.gameimpl.tictactoe.TicTacToeGame;
import xyz.cx233.game.gameimpl.wuziqi.Wuziqi;
import xyz.cx233.game.platform.game.GameModuleFactory;
import xyz.cx233.game.platform.game.GameRegistry;
import xyz.cx233.game.platform.game.api.GameModule;
import xyz.cx233.game.platform.room.Room;

@Component
public class WuziqiFactory implements GameModuleFactory {

    private static final int MIN_PLAYERS = 2;
    public WuziqiFactory(GameRegistry registry) {
        registry.register(this);
    }

    @Override
    public String gameId() {
        return "wuziqi";
    }

    @Override
    public GameModule create() {
        return new Wuziqi();
    }

    @Override
    public GameModule create(Object params) {
        return new Wuziqi(params);
    }

    @Override
    public void gameStartCheck(Room room) {
        allReadyCheck(room);
        minPlayersCheck(room, MIN_PLAYERS);
    }


}
