package xyz.cx233.game.platform.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Data
@AllArgsConstructor
public class Player {
    private String userId;
    private String imageUrl;
    private WebSocketSession session;
    private boolean ready;
    private Room room;
    private boolean connected;
    private long lastSeen;

    public void sendMessage(String jsonMsg) throws IOException {
        if(isConnected()){
            try {
                session.sendMessage(new TextMessage(jsonMsg));
            }catch (Exception e){
                log.error("发送失败，player:{}", userId, e);
                room.onDisconnect(userId);
            }
        }
    }

}
