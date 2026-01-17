package xyz.cx233.game.platform.exception.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.cx233.game.platform.exception.BaseException;

@Data
public class RoomException extends BaseException {
    private String message;

    public RoomException(String message) {
        super(message);
    }

    public RoomException(String message, Object... args) {
        super(message, args);
    }
}
