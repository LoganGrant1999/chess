package ui;

import server.ServerFacade;

public class GameplayClient {

    private final ServerFacade server;

    private final String authToken;

    private final int gameID;

    private final String playerColor;


    public GameplayClient(String serverUrl, String authToken, int gameID, String playerColor) {

        server = new ServerFacade(serverUrl);

        this.authToken = authToken;

        this.gameID = gameID;

        this.playerColor = playerColor;
    }


    public String eval(String input) {

        return null;
    }


    public String drawBoard() {

        return null;
    }

}
