package exceptions;

/**
 * Indicates the client tried to register a new user with an existing username
 */

public class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException(String message) {
        super(message);
    }
}
