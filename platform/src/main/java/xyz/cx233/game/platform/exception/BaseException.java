package xyz.cx233.game.platform.exception;

public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }
    public BaseException(String message, Object... args) {
        super(String.format(message, args));
    }

}
