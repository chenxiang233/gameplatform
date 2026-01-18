package xyz.cx233.game.platform.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.cx233.game.platform.game.GameManager;

@Data
@AllArgsConstructor
public class RoomTask {

    private Room room;

    private GameManager gameManager;

    public void start(){

    }

    public void close(){

    }

}
