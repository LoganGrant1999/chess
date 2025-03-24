package ui;

import exceptions.NetworkException;
import model.ListGameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.ListGamesResponse;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PostLoginClient {

    private int gameID;

    private String playerColor;

    private final ServerFacade server;

    private final String authToken;

    private ArrayList<ListGameData> currList;


    public PostLoginClient(String serverUrl, String authToken) {

        server = new ServerFacade(serverUrl);

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

                case "list" -> listGames(params);

                case "play" -> playGame(params);

                case "observe" -> observeGame(params);

                case "logout" -> logout(params);

                case "quit" -> "quit";

                default -> "Command unknown. Type 'help' to see all valid commands";
            };

        } catch (NetworkException e) {

            return e.getMessage();
        }
    }


    public String listGames(String... params) throws NetworkException {

        if (params.length == 0) {

            int counter = 0;

            ArrayList<ListGameData> displayGames = new ArrayList<>();

            ListGamesResponse resp = server.listGames(authToken);

            currList = resp.games();

            for (ListGameData game: resp.games()) {

                counter = counter + 1;

                displayGames.add(new ListGameData(counter,
                        game.whiteUsername(), game.blackUsername(), game.gameName()));
            }

            var result = new StringBuilder();

            for ( ListGameData game : displayGames) {

                result.append(game.gameID() + ". " + game.gameName().toUpperCase() +

                        " | WhiteUser: " + game.whiteUsername() + " | BlackUser: " + game.blackUsername() + "\n");
            }

            return result.toString();
        }

        throw new NetworkException(400, "Expected no arguments!");
    }


    public String observeGame(String... params) throws NetworkException {

        if (currList.isEmpty()){

            throw new NetworkException(400, "There are no current games");
        }

        if (params.length == 1 && Integer.parseInt(params[0]) <= currList.size()) {

            int actualID = Integer.parseInt(params[0]);

            ListGameData game = currList.get(actualID - 1);

            gameID = game.gameID();

            JoinGameRequest req = new JoinGameRequest(null, game.gameID());

            playerColor = params[0];

            server.joinGame(req, authToken);

            return String.format("You Are Observing the Game!");
        }

        throw new NetworkException(400, "Expected: <ID> <WHITE|BLACK>");

    }


    public String playGame(String... params) throws NetworkException {

        if (currList.isEmpty()){

            throw new NetworkException(400, "There are no current games");
        }


        if (params.length == 2 && (Objects.equals(params[1], "black") || Objects.equals(params[1], "white"))
                && Integer.parseInt(params[0]) <= currList.size()) {

            int actualID = Integer.parseInt(params[0]);

            ListGameData game = currList.get(actualID - 1);

            gameID = game.gameID();

            JoinGameRequest req = new JoinGameRequest(params[1], game.gameID());

            playerColor = params[1];

            server.joinGame(req, authToken);

            return String.format("You Successfully Joined the Game");
        }

        throw new NetworkException(400, "Expected: <ID> <WHITE|BLACK>");

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


    public String getAuthToken() {

        return authToken;
    }


    public String getPlayerColor() {

        return playerColor;
    }

    public int getGameID() {

        return gameID;
    }
}
