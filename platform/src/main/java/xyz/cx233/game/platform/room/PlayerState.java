package xyz.cx233.game.platform.room;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerState {
    private String userId;
    private boolean ready;

    private boolean connected;

}
