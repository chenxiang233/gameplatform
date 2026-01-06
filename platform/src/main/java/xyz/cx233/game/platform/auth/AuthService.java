package xyz.cx233.game.platform.auth;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TokenService tokenService;

    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public LoginResult login(String userId) {
        String token = tokenService.generateToken(userId);
        return new LoginResult(userId, token);
    }
}
