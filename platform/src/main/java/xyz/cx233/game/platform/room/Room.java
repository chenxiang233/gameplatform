package xyz.cx233.game.platform.room;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class Room {
    private int version = 0;
    private final String roomId;
    private final String ownerId;
    private RoomState state = RoomState.WAITING;

    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        players.put(player.getUserId(), player);
        if(ownerId.equals(player.getUserId())){
            player.setReady(true);
        }
    }

    public void removePlayer(String userId) {
        players.remove(userId);
    }

    public boolean contains(String userId) {
        return players.containsKey(userId);
    }

    public Collection<Player> allPlayers() {
        return players.values();
    }

    public boolean isOwner(String userId) {
        return ownerId.equals(userId);
    }

    public void setReady(String userId, boolean ready) {
        Player p = players.get(userId);
        if (p != null) {
            p.setReady(ready);
        }
    }

    public boolean allReadyAndConnected() {
        return players.values().stream()
                .allMatch(p -> p.isReady() && p.isConnected());
    }

    public List<PlayerState> buildPlayerStates() {
        return players.values().stream()
                .map(p -> new PlayerState(
                        p.getUserId(),
                        p.getImageUrl(),
                        p.isReady(),
                        p.isConnected()
                ))
                .collect(Collectors.toList());
    }

    public void onDisconnect(String userId) {
        Player p = players.get(userId);
        if (p != null) {
            p.setConnected(false);
            p.setLastSeen(System.currentTimeMillis());
        }
    }

    public void onReconnect(String userId, WebSocketSession session) {
        Player p = players.get(userId);
        if (p != null) {
            p.setSession(session);
            p.setConnected(true);
        }
    }

    public void sweepOffline(long timeoutMs) {
        long now = System.currentTimeMillis();
        players.values().removeIf(p ->
                !p.isConnected()
                        && now - p.getLastSeen() > timeoutMs
        );
    }

    public boolean hasPlayer(String userId) {
        return players.containsKey(userId);
    }

    public int change(){
        return ++version;
    }
}
