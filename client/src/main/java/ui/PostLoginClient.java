package ui;

import exceptions.NetworkException;
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

                case "logout" -> logout(params);

                case "create game" -> createGame(params);

                case "list games" -> listGames(params);

                case "play game" -> playGame(params);

                case "observe game" -> observeGame(params);

                default -> "Command unknown. Type 'help' to see all valid commands";
            };

        } catch (NetworkException e) {

            return e.getMessage();
        }
    }







}
