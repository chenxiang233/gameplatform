package xyz.cx233.game.platform.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {
    private String userId;
    private String token;
}
