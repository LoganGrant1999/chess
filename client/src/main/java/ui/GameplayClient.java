package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import server.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;


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


    public void printPieces(PrintStream out, ChessPiece[][] board,  int x, int y) {

        ChessPiece piece = board[x][y];

        if (piece == null){

            out.print(EMPTY);

            return;
        }

        ChessGame.TeamColor color = piece.getTeamColor();

        ChessPiece.PieceType type = piece.getPieceType();

        if (Objects.equals(color, BLACK) && Objects.equals(type, PAWN)) {

            out.print(EscapeSequences.BLACK_PAWN);

        } else if (Objects.equals(color, BLACK) && Objects.equals(type, ROOK)) {

            out.print(EscapeSequences.BLACK_ROOK);

        } else if (Objects.equals(color, BLACK) && Objects.equals(type, KNIGHT)){

            out.print(EscapeSequences.BLACK_KNIGHT);

        } else if (Objects.equals(color, BLACK) && Objects.equals(type, BISHOP)) {

            out.print(EscapeSequences.BLACK_BISHOP);

        } else if (Objects.equals(color, BLACK) && Objects.equals(type, QUEEN)) {

            out.print(EscapeSequences.BLACK_QUEEN);

        } else if (Objects.equals(color, BLACK) && Objects.equals(type, KING)) {

            out.print(EscapeSequences.BLACK_KING);

        } else if (Objects.equals(color, WHITE) && Objects.equals(type, PAWN)) {

            out.print(EscapeSequences.WHITE_PAWN);

        } else if (Objects.equals(color, WHITE) && Objects.equals(type, ROOK)) {

            out.print(EscapeSequences.WHITE_ROOK);

        } else if (Objects.equals(color, WHITE) && Objects.equals(type, KNIGHT)){

            out.print(EscapeSequences.WHITE_KNIGHT);

        } else if (Objects.equals(color, WHITE) && Objects.equals(type, BISHOP)) {

            out.print(EscapeSequences.WHITE_BISHOP);

        } else if (Objects.equals(color, WHITE) && Objects.equals(type, QUEEN)) {

            out.print(EscapeSequences.WHITE_QUEEN);

        } else if (Objects.equals(color, WHITE) && Objects.equals(type, KING)) {

            out.print(EscapeSequences.WHITE_KING);
        }
    }


    public void colorSquares(PrintStream out, ChessBoard board) {

        ArrayList<String> letters = new ArrayList<>(Arrays.asList
                (" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "));

        ArrayList<String> nums = new ArrayList<>(Arrays.asList
                (" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "));

        ChessPiece[][] boardDisplay = board.getBoard();

        if (Objects.equals(playerColor, "WHITE") || playerColor == null) {

            out.print(EMPTY);

            for (int i = 0; i < letters.size(); i++){

                out.print(letters.get(i));
            }

            out.println();

            for (int x = 7; x >= 0; x--) {

                out.print(nums.get(x));

                for (int y = 0; y < boardDisplay[0].length; y++) {

                    loopThroughSquares(out, boardDisplay, x, y);

                }

                out.print(EscapeSequences.RESET_BG_COLOR);

                out.println();
            }

        } else if (Objects.equals(playerColor, "BLACK")) {

            out.print(EMPTY);

            for (int i = 7; i >= 0; i--) {

                out.print(letters.get(i));
            }

            out.println();

            for (int x = 0; x <= 7; x++) {

                out.print(nums.get(x));

                for (int y = 7; y >= 0; y--) {

                    loopThroughSquares(out, boardDisplay, x, y);

                }

                out.print(EscapeSequences.RESET_BG_COLOR);

                out.println();
            }
        }
    }


    private void loopThroughSquares(PrintStream out, ChessPiece[][] boardDisplay, int x, int y) {

            if ((x + y) % 2 == 0) {

                out.print(EscapeSequences.SET_BG_COLOR_MAGENTA);

                printPieces(out, boardDisplay, x, y);

            } else {

                out.print(EscapeSequences.SET_BG_COLOR_WHITE);

                printPieces(out, boardDisplay, x, y);
            }
    }

}



