package xyz.cx233.game.platform.ws;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.cx233.game.platform.handler.*;
import xyz.cx233.game.platform.protocol.WsMessage;
import xyz.cx233.game.platform.protocol.WsType;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Component
public class WsDispatcher {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<WsType, WsHandler> handlers = new EnumMap<>(WsType.class);

    public WsDispatcher(PingHandler pingHandler,
                        JoinRoomHandler joinRoomHandler,
                        LeaveRoomHandler leaveRoomHandler,
                        ReadyHandler readyHandler,
                        StartGameHandler startGameHandler,
                        GameActionHandler gameActionHandler,
                        StopGameHandler stopGameHandler) {
        handlers.put(WsType.PING, pingHandler);
        handlers.put(WsType.JOIN_ROOM, joinRoomHandler);
        handlers.put(WsType.LEAVE_ROOM, leaveRoomHandler);
        handlers.put(WsType.READY, readyHandler);
        handlers.put(WsType.START_GAME, startGameHandler);
        handlers.put(WsType.STOP_GAME, stopGameHandler);
        handlers.put(WsType.GAME_ACTION, gameActionHandler);
    }

    public void dispatch(String text, WsSession session) {
        try {
            WsMessage message = mapper.readValue(text, WsMessage.class);
            message.setUserId(session.getUserId());

            WsHandler handler = handlers.get(message.getType());
            if(message.getType()!=WsType.PING){
                log.info("[ws-client] dispatch message:{}",message);
            }
            if (handler == null) {
                return;
            }

            handler.handle(message, session.getSession());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
