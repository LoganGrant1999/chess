package exceptions;

/**
 * Indicates the client tried to register a new user with an existing username
 */

public class MissingDataException extends RuntimeException {
    public MissingDataException(String message) {
        super(message);
    }
}
