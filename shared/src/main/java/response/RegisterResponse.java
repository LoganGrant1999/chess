package response;

public class RegisterResponse extends Response {

    private String username;
    private String authToken;

    public RegisterResponse(String username, String authToken, int statusCode, String message) {
        this.username = username;
        this.authToken = authToken;
        this.statusCode = statusCode;
        this.message = message;
    }
}
