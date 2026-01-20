package xyz.cx233.game.platform.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import xyz.cx233.game.platform.exception.BaseException;
import xyz.cx233.game.platform.game.GameManager;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;
import xyz.cx233.game.platform.room.Player;
import xyz.cx233.game.platform.room.Room;
import xyz.cx233.game.platform.room.RoomManager;
import xyz.cx233.game.platform.room.RoomState;

@Component
public class StopGameHandler implements WsHandler {

    private final RoomManager roomManager;
    private final ObjectMapper mapper = new ObjectMapper();
    private final GameManager gameManager;

    public StopGameHandler(RoomManager roomManager,
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
            sendError(session, "房主点击");
            return;
        }

        // ② 状态校验
        if (room.getState() != RoomState.PLAYING) return;
        // ④ 状态切换
        try {
            gameManager.stopGame(roomId);
        } catch (BaseException e) {
            sendError(session, e.getMessage());
            return;
        }
        room.setState(RoomState.WAITING);
        room.change();
        broadcastGameStop(room);

    }

    private void broadcastGameStop(Room room) throws Exception {

        WsMessage msg = new WsMessage();
        msg.setType(WsType.STOP_GAME);
        msg.setRoomId(room.getRoomId());
        msg.setVersion(room.getVersion());
        String json = mapper.writeValueAsString(msg);
        for (Player p : room.allPlayers()) {
            p.sendMessage(json);
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
