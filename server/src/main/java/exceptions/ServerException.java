package exceptions;

/**
 * Indicates a problem with the server connecting to the database
 */


public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
