package xyz.cx233.game.platform.ws;


import xyz.cx233.game.platform.handler.PingHandler;
import xyz.cx233.game.platform.handler.WsHandler;
import xyz.cx233.game.platform.protocol.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.EnumMap;
import java.util.Map;

@Component
public class WsDispatcher {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<WsType, WsHandler> handlers = new EnumMap<>(WsType.class);

    public WsDispatcher(PingHandler pingHandler) {
        handlers.put(WsType.PING, pingHandler);
    }

    public void dispatch(String text, WebSocketSession session) {
        try {
            WsMessage message = mapper.readValue(text, WsMessage.class);
            WsHandler handler = handlers.get(message.getType());

            if (handler == null) {
                sendError(session, message.getRequestId(),
                        ErrorCode.INVALID_MESSAGE, "Unsupported type");
                return;
            }

            handler.handle(message, session);

        } catch (Exception e) {
            sendError(session, null,
                    ErrorCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    private void sendError(WebSocketSession session,
                           String requestId,
                           ErrorCode code,
                           String msg) {
        try {
            WsMessage error = new WsMessage();
            error.setType(WsType.ERROR);
            error.setRequestId(requestId);
            error.setPayload(new ErrorResponse(code, msg));

            session.sendMessage(
                    new TextMessage(mapper.writeValueAsString(error))
            );
        } catch (Exception ignored) {
        }
    }
}
