package xyz.cx233.game.platform.game.api;


import xyz.cx233.game.platform.game.GameModule;
import xyz.cx233.game.platform.game.frame.FrameInput;
import xyz.cx233.game.platform.game.GameBroadcaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FrameSyncGame implements GameModule {

    protected final List<FrameInput> frameBuffer = new ArrayList<>();

    @Override
    public void onPlayerAction(String userId, Object action) {
        frameBuffer.add(new FrameInput(userId, action));
    }

    /**
     * 由平台定时调用
     */
    public void tick(GameBroadcaster broadcaster) {
        broadcaster.broadcast(Map.of(
                "type", "FRAME_SYNC",
                "inputs", frameBuffer
        ));
        frameBuffer.clear();
    }
}
