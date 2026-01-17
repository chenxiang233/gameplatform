package xyz.cx233.game.platform.handler;

import xyz.cx233.game.platform.auth.AuthService;
import xyz.cx233.game.platform.auth.TokenService;
import xyz.cx233.game.platform.game.GameManager;
import xyz.cx233.game.platform.game.GameRuntime;
import xyz.cx233.game.platform.game.api.SnapshotAwareGame;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;
import xyz.cx233.game.platform.room.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.stream.Collectors;

@Component
public class JoinRoomHandler implements WsHandler {

    private final RoomManager roomManager;

    private final GameManager gameManager;

    private final AuthService authService;


    public JoinRoomHandler(RoomManager roomManager, GameManager gameManager, AuthService authService) {
        this.roomManager = roomManager;
        this.gameManager = gameManager;
        this.authService = authService;
    }

    @Override
    public void handle(WsMessage message, WebSocketSession session)
            throws Exception {

        String userId = message.getUserId();
        String roomId = message.getRoomId();

        Room room = roomManager.getRoom(roomId);
        if (room == null) {
            // 第一个人 = 创建房间
            room = roomManager.createRoom(roomId, userId);
        }

        if (!room.contains(userId)) {
            room.addPlayer(new Player(userId, authService.getUser(userId).getImageUrl(),
                    session, false, true, System.currentTimeMillis()));
        }else{
            room.onReconnect(userId, session);
        }
        roomManager.broadcastRoomState(room);
        GameRuntime runtime = gameManager.getGame(roomId);
        if (runtime != null) {
            if(runtime.getGame() instanceof SnapshotAwareGame){
                ((SnapshotAwareGame) runtime.getGame()).broadcastSnapshot();
            }
        }

    }

}
