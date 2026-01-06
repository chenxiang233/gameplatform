package xyz.cx233.game.platform.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode code;
    private String message;
}
