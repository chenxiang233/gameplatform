package xyz.cx233.game.platform.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import xyz.cx233.game.platform.room.Player;
import xyz.cx233.game.platform.room.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WsGameBroadcaster implements GameBroadcaster {

    private final Room room;
    private final ObjectMapper mapper = new ObjectMapper();

    public WsGameBroadcaster(Room room) {
        this.room = room;
    }

    @Override
    public void broadcast(Map<String, Object> gameState) {
        gameState = new HashMap<>(gameState);
        gameState.put("version", room.getVersion());
        String json = null;
        try {
            json = mapper.writeValueAsString(gameState);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (Player p : room.allPlayers()) {
            p.sendMessage(json);
        }

    }

    @Override
    public void sendTo(String userId, Object msg) {
        try {
            String json = mapper.writeValueAsString(msg);
            Player p = room.getPlayers().get(userId);
            if (p != null) {
                p.sendMessage(json);
            }
        } catch (Exception e) {
            // log
        }
    }

    /**
     * 发送游戏快照
     *
     * @param gameType 游戏类型
     * @param snapshot 游戏快照
     */
    @Override
    public void sendSnapshot(String gameType, Object snapshot) {
        broadcast(Map.of(
                "type", "GAME_SNAPSHOT",
                "game", gameType,
                "state", snapshot
        ));
    }

}
