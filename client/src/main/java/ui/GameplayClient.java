package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import exceptions.NetworkException;
import server.NotificationHandler;
import server.WebSocketServerFacade;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;


public class GameplayClient implements NotificationHandler {

    private ChessGame game;

    private static final String EMPTY = "   ";

    private final String authToken;

    private final int gameID;

    private final String playerColor;

    private WebSocketServerFacade ws;

    public GameplayClient(String serverUrl, String authToken, int gameID, String playerColor) throws NetworkException {

        this.ws = new WebSocketServerFacade(serverUrl, this );

        ws.connectToGame(authToken, gameID, playerColor);

        this.authToken = authToken;

        this.gameID = gameID;

        if (playerColor != null) {

            this.playerColor = playerColor.toUpperCase();

        } else {

        this.playerColor = playerColor;

        }

    }

    public String eval(String input) {

        try {

            var tokens = input.toLowerCase().split(" ");

            var cmd = (tokens.length > 0) ? tokens[0] : "help";

            return switch (cmd) {

                case "help" -> help();
                case "redraw" -> drawBoard();
                case "leave" -> leave();
                case "move" -> move();
                case "resign" -> resign();
                case "highlight" -> highlight();
                case "quit" -> "quit";
                default -> "Command unknown. Type 'help' to see all valid commands";

            };

        } catch (NumberFormatException e) {

            return "Expected a number as an input";

        } catch (IndexOutOfBoundsException e) {

            return "Error: invalid game number";

        } catch (NetworkException e) {

            return "Error: Could not leave game";
        }
    }

    public String drawBoard() {

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        out.print("\n");

        ChessBoard board = game.getBoard();

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

    @Override
    public void notify(ServerMessage message) throws NetworkException {

        try {

            switch (message.getServerMessageType()) {

                case LOAD_GAME -> {

                    this.game = message.getGame();

                    drawBoard();

                    System.out.print("\n>>> ");
                }

                case NOTIFICATION -> {
                    System.out.println(message.getMsg());
                    System.out.print("\n>>> ");
                }

                case ERROR -> {
                    System.out.print(message.getMsg());
                    System.out.print("\n>>> ");
                }
            }

        } catch (Exception e) {
            throw new NetworkException(500, e.getMessage());
        }

    }


    public String help(){

        return """
            - Help
            - Redraw
            - Leave
            - Make Move <start position> <end position> <optional promotion> (e.g. f5 e4 q)
            - Resign
            - Highlight <position> (e.g. f5)
            - Quit
            """;
    }


    public String leave() throws NetworkException {

        ws.leaveGame(authToken, gameID);

        return "You successfully left the game!";
    }


    public String move(){

        return null;

    }

    public String resign(){
        return null;
    }

    public String highlight(){

        return null;

    }



}