package xyz.cx233.game.platform.auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private final Map<String, String> tokenUserMap = new ConcurrentHashMap<>();

    public String generateToken(String userId) {
        String token = getToken(userId);
        tokenUserMap.put(token, userId);
        return token;
    }

    public String verify(String token) {
        return getUserId(token);
    }

    private String getToken(String userId){
        return UUID.randomUUID().toString();
    }

    private String getUserId(String token){
        return tokenUserMap.get(token);
    }
}
