package xyz.cx233.game.platform.auth;

import org.springframework.stereotype.Service;
import xyz.cx233.game.platform.auth.model.UserDto;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final TokenService tokenService;

    private final Map<String, UserDto> userMap;

    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
        this.userMap = new HashMap<>();
    }

    public LoginResult login(UserDto userDto) {
        String userId = userDto.getUserId();
        String token = tokenService.generateToken(userId);
        userMap.put(userId, userDto);
        return new LoginResult(userId, token);
    }

    public UserDto getUser(String userId) {
        return userMap.get(userId);
    }
}
