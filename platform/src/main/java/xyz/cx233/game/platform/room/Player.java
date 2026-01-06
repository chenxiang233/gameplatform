package xyz.cx233.game.platform.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
public class Player {
    private String userId;
    private WebSocketSession session;
    private boolean ready;
}
