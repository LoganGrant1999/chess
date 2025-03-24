package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import server.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.EmptyStackException;
import java.util.Objects;


public class GameplayClient {

    private static final String EMPTY = "   ";

    private final ServerFacade server;

    private final String authToken;

    private final int gameID;

    private final String playerColor;


    public GameplayClient(String serverUrl, String authToken, int gameID, String playerColor) {

        server = new ServerFacade(serverUrl);

        this.authToken = authToken;

        this.gameID = gameID;


        if (playerColor != null) {

            this.playerColor = playerColor.toUpperCase();

        } else {

        this.playerColor = playerColor;
        }
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

        return "";
    }


    public void colorSquares(PrintStream out, ChessBoard board){

        ChessPiece[][] boardDisplay = board.getBoard();

        if (Objects.equals(playerColor, "WHITE") || playerColor == null) {

            for (int x = 7; x >= 0; x--) {

                loopThroughCols(out, boardDisplay, x);

                out.print(EscapeSequences.RESET_BG_COLOR);

                out.println();

            }

        } else if (Objects.equals(playerColor, "BLACK")) {

            for (int x = 0; x <= 7; x++) {

                loopThroughCols(out, boardDisplay, x);

                out.print(EscapeSequences.RESET_BG_COLOR);

                out.println();

            }
        }
    }

    private void loopThroughCols(PrintStream out, ChessPiece[][] boardDisplay, int x) {
        for (int y = 0; y < boardDisplay[0].length; y++) {

            if ((x + y) % 2 == 0) {

                out.print(EscapeSequences.SET_BG_COLOR_MAGENTA);

                out.print(EMPTY);

            } else {

                out.print(EscapeSequences.SET_BG_COLOR_WHITE);

                out.print(EMPTY);
            }

        }
    }

}



