package xyz.cx233.game.platform.handler;

import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PingHandler implements WsHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(WsMessage message, WebSocketSession session)
            throws Exception {

        WsMessage pong = new WsMessage();
        pong.setType(WsType.PONG);
        pong.setRequestId(message.getRequestId());
        pong.setUserId(message.getUserId());
        pong.setTimestamp(System.currentTimeMillis());

        session.sendMessage(
                new TextMessage(mapper.writeValueAsString(pong))
        );
    }

}
