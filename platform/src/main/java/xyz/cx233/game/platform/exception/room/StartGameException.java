package xyz.cx233.game.platform.exception.room;

public class StartGameException extends RoomException{
    public StartGameException(String message) {
        super(message);
    }
    public StartGameException(String message, Object... args) {
        super(message, args);
    }
}
