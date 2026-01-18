package xyz.cx233.game.platform.handler;


import xyz.cx233.game.platform.game.GameManager;
import xyz.cx233.game.platform.game.GameRuntime;
import xyz.cx233.game.platform.handler.WsHandler;
import xyz.cx233.game.platform.protocol.WsMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class GameActionHandler implements WsHandler {

    private final GameManager gameManager;

    public GameActionHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void handle(WsMessage message, WebSocketSession session)
            throws Exception {

        String roomId = message.getRoomId();
        String userId = message.getUserId();
        GameRuntime runtime = gameManager.getGame(roomId);
        if (runtime == null) return;
        runtime.getRoom().change();
        runtime.getGame().onPlayerAction(
                userId,
                message.getPayload()
        );
    }
}
