package xyz.cx233.game.platform.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameContext {

    private String roomId;
    private List<String> playerIds;
}
