package xyz.cx233.game.platform.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Data
@AllArgsConstructor
public class Player {
    private String userId;
    private String imageUrl;
    private WebSocketSession session;
    private boolean ready;

    private boolean connected;
    private long lastSeen;

    public void sendMessage(String jsonMsg) throws IOException {
        if(isConnected()){
            session.sendMessage(new TextMessage(jsonMsg));
        }
    }
}
