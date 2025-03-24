package ui;

import chess.ChessBoard;
import server.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.System.out;

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

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        ChessBoard board = new ChessBoard();

        board.resetBoard();





        return null;

    }

}
