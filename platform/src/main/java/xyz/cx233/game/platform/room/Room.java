package xyz.cx233.game.platform.room;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class Room {

    private final String roomId;
    private final String ownerId;
    private RoomState state = RoomState.WAITING;

    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        players.put(player.getUserId(), player);
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

    public boolean allReady() {
        return players.values().stream()
                .allMatch(Player::isReady);
    }

    public List<PlayerState> buildPlayerStates() {
        return players.values().stream()
                .map(p -> new PlayerState(
                        p.getUserId(),
                        p.isReady()
                ))
                .collect(Collectors.toList());
    }

}
