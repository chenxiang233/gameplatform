package xyz.cx233.game.platform.game;

import xyz.cx233.game.platform.room.Player;
import xyz.cx233.game.platform.room.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;

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
}
