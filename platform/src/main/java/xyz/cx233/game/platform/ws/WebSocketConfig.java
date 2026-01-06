package xyz.cx233.game.platform.ws;


import xyz.cx233.game.platform.ws.WsEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WsEndpoint wsEndpoint;

    public WebSocketConfig(WsEndpoint wsEndpoint) {
        this.wsEndpoint = wsEndpoint;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsEndpoint, "/ws")
                .setAllowedOrigins("*");
    }
}

