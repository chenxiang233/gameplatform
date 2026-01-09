package xyz.cx233.game.platform.game.scheduler;

import xyz.cx233.game.platform.game.api.FrameSyncGame;
import xyz.cx233.game.platform.game.GameManager;
import xyz.cx233.game.platform.game.GameRuntime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FrameTickScheduler {

    private final GameManager gameManager;

    public FrameTickScheduler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * 10 FPS（演示用）
     */
//    @Scheduled(fixedRate = 100)
//    public void tick() {
//        for (GameRuntime runtime : gameManager.getRunningGames()) {
//            if (runtime.getGame() instanceof FrameSyncGame frameGame) {
//                frameGame.tick(runtime.getBroadcaster());
//            }
//        }
//    }
}
