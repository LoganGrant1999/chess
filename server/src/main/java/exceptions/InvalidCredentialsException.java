package exceptions;

/**
 * Indicates the client did not provide valid credentials for an action
 */

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
