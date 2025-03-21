package ui;

import exceptions.NetworkException;
import request.LoginRequest;
import response.LoginResponse;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Locale;

public class PreLoginClient {


    private final ServerFacade server;

    private final String serverUrl;

    public PreLoginClient(String serverUrl) {

        server = new ServerFacade(serverUrl);

        this.serverUrl = serverUrl;
    }

    public String eval(String input){

        try {

            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "quit" -> "quit";
                case "login" -> login(params);
                case "register" -> register(params);
                default -> "Command unknown. Type 'help' to see all valid commands";
            };
        } catch (NetworkException e) {

            return e.getMessage();
        }
    }

    public String help() {

        return """
                - Help
                - Quit
                - Login <username> <password>
                - Register <username> <password> <email>
                """;
    }

    public String login(String... params) throws NetworkException {

        if (params.length == 2) {

            LoginRequest req = new LoginRequest(params[0], params[1]);

            LoginResponse resp = server.login(req);

            return String.format("You signed in as %s!", resp.username());
        }

        throw new NetworkException(400, "Expected: <username> <password>");
    }

    public String register(String... params) {

        return null;
    }



}
