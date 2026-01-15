package xyz.cx233.game.platform.game;

import xyz.cx233.game.platform.room.Player;
import xyz.cx233.game.platform.room.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;

import java.util.Map;

public class WsGameBroadcaster implements GameBroadcaster {

    private final Room room;
    private final ObjectMapper mapper = new ObjectMapper();

    public WsGameBroadcaster(Room room) {
        this.room = room;
    }

    @Override
    public void broadcast(Object gameState) {
        try {
            String json = mapper.writeValueAsString(gameState);
            for (Player p : room.allPlayers()) {
                p.getSession().sendMessage(new TextMessage(json));
            }
        } catch (Exception e) {
            // log
        }
    }

    @Override
    public void sendTo(String userId, Object msg) {
        try {
            String json = mapper.writeValueAsString(msg);
            Player p = room.getPlayers().get(userId);
            if (p != null) {
                p.getSession().sendMessage(new TextMessage(json));
            }
        } catch (Exception e) {
            // log
        }
    }

    /**
     * 发送游戏快照
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
