package xyz.cx233.game.platform.room;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class RoomCleanupTask {


    private final RoomManager roomManager;

    public RoomCleanupTask(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Scheduled(fixedRate = 10_000)
    public void cleanup() {
        for (Room room : roomManager.allRooms()) {
            room.sweepOffline(600_000);
            if (room.allPlayers().isEmpty()) {
                roomManager.removeRoom(room.getRoomId());
            }
        }
    }
}
