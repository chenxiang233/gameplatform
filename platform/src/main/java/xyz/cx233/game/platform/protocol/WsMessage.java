package xyz.cx233.game.platform.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsMessage {

    private WsType type;
    private String requestId;
    private Long timestamp;

    private String roomId;
    private String userId;
    private String gameId;

    private Object payload;
}
