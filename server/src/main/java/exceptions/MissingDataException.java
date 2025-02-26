package exceptions;

/**
 * Indicates the client did not provide sufficient data to carry out an action
 */

public class MissingDataException extends RuntimeException {
    public MissingDataException(String message) {
        super(message);
    }
}
