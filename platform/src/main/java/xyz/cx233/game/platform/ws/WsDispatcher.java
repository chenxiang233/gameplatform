package xyz.cx233.game.platform.ws;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import xyz.cx233.game.platform.handler.*;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;

import java.util.EnumMap;
import java.util.Map;

@Component
public class WsDispatcher {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<WsType, WsHandler> handlers = new EnumMap<>(WsType.class);

    public WsDispatcher(PingHandler pingHandler,
                        JoinRoomHandler joinRoomHandler,
                        LeaveRoomHandler leaveRoomHandler,
                        ReadyHandler readyHandler,
                        StartGameHandler startGameHandler,
                        GameActionHandler gameActionHandler) {
        handlers.put(WsType.PING, pingHandler);
        handlers.put(WsType.JOIN_ROOM, joinRoomHandler);
        handlers.put(WsType.LEAVE_ROOM, leaveRoomHandler);
        handlers.put(WsType.READY, readyHandler);
        handlers.put(WsType.START_GAME, startGameHandler);
        handlers.put(WsType.GAME_ACTION, gameActionHandler);

    }

    public void dispatch(String text, WsSession session) {
        try {
            WsMessage message = mapper.readValue(text, WsMessage.class);
            message.setUserId(session.getUserId());

            WsHandler handler = handlers.get(message.getType());
            if (handler == null) {
                return;
            }

            handler.handle(message, session.getSession());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
