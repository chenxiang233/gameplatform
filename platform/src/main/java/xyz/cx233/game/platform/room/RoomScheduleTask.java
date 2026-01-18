package xyz.cx233.game.platform.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import xyz.cx233.game.platform.game.GameManager;
import xyz.cx233.game.platform.game.GameRuntime;
import xyz.cx233.game.platform.game.api.SnapshotAwareGame;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;

import java.util.Optional;


@Component
@Slf4j
public class RoomScheduleTask {

    ObjectMapper mapper = new ObjectMapper();
    private final RoomManager roomManager;
    private final GameManager gameManager;
    public RoomScheduleTask(RoomManager roomManager, GameManager gameManager) {
        this.roomManager = roomManager;
        this.gameManager = gameManager;
    }

    @Scheduled(fixedRate = 10_000)
    public void cleanup() {
        for (Room room : roomManager.allRooms()) {
            room.sweepOffline(600_000);
            if (room.allPlayers().isEmpty()) {
                roomManager.removeRoom(room.getRoomId());
            }
        }
    }

    @Scheduled(fixedRate = 10_000)
    public void sync(){
        log.info("定时同步房间状态...");
        for (Room room : roomManager.allRooms()) {
            try {
                log.info("同步房间状态room:{}",room);
                broadcastRoom(room);
            } catch (Exception e) {
                log.error("同步房间状态异常",e);
            }

        }
    }

    public void broadcastRoom(Room room) throws Exception {
        GameRuntime gameRuntime = gameManager.getGame(room.getRoomId());
        Object snapshot = null;
        if(Optional.ofNullable(gameRuntime.getGame()).
                filter(game->game instanceof SnapshotAwareGame).isPresent()){
            SnapshotAwareGame snapshotAwareGame = (SnapshotAwareGame) gameRuntime.getGame();
            snapshot = snapshotAwareGame.snapshot();
        }
        GameStatePayload payload = new GameStatePayload(
                room.getRoomId(),
                room.getOwnerId(),
                room.getState(),
                room.buildPlayerStates(),
                snapshot
        );

        WsMessage msg = new WsMessage();
        msg.setType(WsType.GAME_SYNC);
        msg.setRoomId(room.getRoomId());
        msg.setPayload(payload);
        msg.setVersion(room.getVersion());
        String json = mapper.writeValueAsString(msg);
        for (Player p : room.allPlayers()) {
            p.sendMessage(json);
        }
    }
}
