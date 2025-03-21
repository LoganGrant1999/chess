package ui;

import exceptions.NetworkException;
import request.CreateGameRequest;
import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient {

    private final ServerFacade server;

    private final String authToken;

    private final String serverUrl;


    public PostLoginClient(String serverUrl, String authToken) {

        server = new ServerFacade(serverUrl);

        this.serverUrl = serverUrl;

        this.authToken = authToken;
    }

    public String eval(String input){

        try {

            var tokens = input.toLowerCase().split(" ");

            var cmd = (tokens.length > 0) ? tokens[0] : "help";

            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {

                case "help" -> help();

                case "create" -> createGame(params);

                //case "list" -> listGames(params);

                //case "play" -> playGame(params);

                //case "observe" -> observeGame(params);

                case "logout" -> logout(params);

                case "quit" -> "quit";

                default -> "Command unknown. Type 'help' to see all valid commands";
            };

        } catch (NetworkException e) {

            return e.getMessage();
        }
    }



    public String createGame(String... params) throws NetworkException {

        if (params.length == 1) {

            CreateGameRequest req = new CreateGameRequest(params[0]);

            server.createGame(req, authToken);

            return String.format("Successfully created %s", params[0]);

        }

        throw new NetworkException(400, "Expected: <name>");

    }


    public String logout(String... params) throws NetworkException {

        if (params.length == 0){

            server.logout(authToken);

            return "Successfully Logged out!";

        }

        throw new NetworkException(400, "Expected no inputs!");
    }

    public String help() {

        return """
                - Help
                - Create <name>
                - List
                - Play <ID> <WHITE|BLACK>
                - Observe
                - Logout
                - Quit
                """;
    }

}
