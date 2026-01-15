package xyz.cx233.game.platform.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import xyz.cx233.game.platform.game.GameBroadcaster;
import xyz.cx233.game.platform.game.GameManager;
import xyz.cx233.game.platform.game.WsGameBroadcaster;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;
import xyz.cx233.game.platform.room.Player;
import xyz.cx233.game.platform.room.Room;
import xyz.cx233.game.platform.room.RoomManager;
import xyz.cx233.game.platform.room.RoomState;

@Component
public class StartGameHandler implements WsHandler {

    private final RoomManager roomManager;
    private final ObjectMapper mapper = new ObjectMapper();
    private final GameManager gameManager;

    public StartGameHandler(RoomManager roomManager,
                            GameManager gameManager) {
        this.roomManager = roomManager;
        this.gameManager = gameManager;

    }

    @Override
    public void handle(WsMessage message, WebSocketSession session)
            throws Exception {

        String userId = message.getUserId();
        String roomId = message.getRoomId();
        String gameId = message.getGameId();
        Room room = roomManager.getRoom(roomId);
        if (room == null) return;

        // ① 房主校验
        if (!room.isOwner(userId)) {
            sendError(session, "ONLY_OWNER_CAN_START");
            return;
        }

        // ② 状态校验
        if (room.getState() != RoomState.WAITING) return;

        // ③ 全员准备校验
        if (!room.allReadyAndConnected()) {
            sendError(session, "NOT_ALL_READY");
            return;
        }

        // ④ 状态切换
        gameManager.startGame(room, gameId);
        room.setState(RoomState.PLAYING);
        broadcastGameStart(room);

    }

    private void broadcastGameStart(Room room) throws Exception {

        WsMessage msg = new WsMessage();
        msg.setType(WsType.START_GAME);
        msg.setRoomId(room.getRoomId());

        String json = mapper.writeValueAsString(msg);

        for (Player p : room.allPlayers()) {
            p.getSession().sendMessage(new TextMessage(json));
        }
    }

    private void sendError(WebSocketSession session, String code)
            throws Exception {
        WsMessage msg = new WsMessage();
        msg.setType(WsType.ERROR);
        msg.setPayload(code);
        session.sendMessage(new TextMessage(
                mapper.writeValueAsString(msg)
        ));
    }
}
