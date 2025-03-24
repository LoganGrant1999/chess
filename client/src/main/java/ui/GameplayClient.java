package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import server.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


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

        colorSquares(out, board);



    }


    public void colorSquares(PrintStream out, ChessBoard board){

        ChessPiece[][] boardDisplay = board.getBoard();

        if (Objects.equals(playerColor, "white") || Objects.equals(playerColor, null)) {

            for (int x = 7; x >= 0 ; x++){

                for (int y = 0; y < boardDisplay[0].length; y++) {







            }

        }

    }



}
