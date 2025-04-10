package ui;
import chess.*;
import exceptions.NetworkException;
import server.NotificationHandler;
import server.WebSocketServerFacade;
import websocket.messages.ServerMessage;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

public class GameplayClient implements NotificationHandler {

    private ChessGame game;
    private static final String EMPTY = "   ";
    private final String authToken;
    private boolean gameOver = false;
    private final int gameID;
    private WebSocketServerFacade ws;
    private ChessGame.TeamColor teamColor;
    private String playerColor;

    public GameplayClient(String serverUrl, String authToken, int gameID, String playerColor) throws NetworkException {

        this.ws = new WebSocketServerFacade(serverUrl, this );
        this.authToken = authToken;
        this.gameID = gameID;
        ws.connectToGame(authToken, gameID, playerColor);

        if (playerColor != null && playerColor.equalsIgnoreCase("WHITE")) {

            teamColor = WHITE;

            this.playerColor = "WHITE";

        } else if (playerColor != null && playerColor.equalsIgnoreCase("BLACK")){

            this.playerColor = "BLACK";

            teamColor = BLACK;

        } else {

        teamColor = null;

        this.playerColor = null;

        }
    }

    public String eval(String input) {
        try {

            var tokens = input.toLowerCase().split(" ");

            var cmd = (tokens.length > 0) ? tokens[0] : "help";

            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {

                case "help" -> help();
                case "redraw" -> drawBoard(null);
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> {

                    if (teamColor == null) {

                        yield "Observers cannot resign!";

                    } else if (gameOver){

                        yield "Game is Already Over";

                    } else {

                        yield resign();
                    }
                }
                case "highlight" -> highlight(params);
                case "quit" -> "quit";
                default -> "Command unknown. Type 'help' to see all valid commands";

            };

        } catch (NumberFormatException e) {

            return "Expected a number as an input";

        } catch (IndexOutOfBoundsException e) {

            return "Error: invalid game number";

        } catch (NetworkException e) {

            return "Error:" + e.getMessage();
        }
    }

    public String drawBoard(ArrayList<ChessPosition> squaresToHighlight) {

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        out.print("\n");

        ChessBoard board = game.getBoard();

        colorSquares(out, board, squaresToHighlight);

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

    public void colorSquares(PrintStream out, ChessBoard board, ArrayList<ChessPosition> squaresToHighlight) {
        ArrayList<String> letters = new ArrayList<>(Arrays.asList
                (" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "));

        ArrayList<String> nums = new ArrayList<>(Arrays.asList
                (" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "));

        ChessPiece[][] boardDisplay = board.getBoard();

        if (Objects.equals(teamColor, WHITE) || teamColor == null) {

            out.print(EMPTY);

            for (int i = 0; i < letters.size(); i++){

                out.print(letters.get(i));
            }

            out.println();

            for (int x = 7; x >= 0; x--) {

                out.print(nums.get(x));

                for (int y = 0; y < boardDisplay[0].length; y++) {

                    loopThroughSquares(out, boardDisplay, x, y, squaresToHighlight);
                }

                out.print(EscapeSequences.RESET_BG_COLOR);

                out.println();
            }
        } else if (Objects.equals(teamColor, BLACK)) {

            out.print(EMPTY);

            for (int i = 7; i >= 0; i--) {

                out.print(letters.get(i));
            }

            out.println();

            for (int x = 0; x <= 7; x++) {

                out.print(nums.get(x));

                for (int y = 7; y >= 0; y--) {

                    loopThroughSquares(out, boardDisplay, x, y, squaresToHighlight);

                }
                out.print(EscapeSequences.RESET_BG_COLOR);

                out.println();
            }
        }
    }

    private void loopThroughSquares(PrintStream out, ChessPiece[][] boardDisplay, int x, int y,
                                    ArrayList<ChessPosition> squaresToHighlight) {
            if ((x + y) % 2 == 0) {

                out.print(EscapeSequences.SET_BG_COLOR_MAGENTA);

                ChessPosition currPos = new ChessPosition(x + 1, y + 1);

                if (squaresToHighlight != null && squaresToHighlight.contains(currPos)){

                    out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                }

                printPieces(out, boardDisplay, x, y);

            } else {

                out.print(EscapeSequences.SET_BG_COLOR_WHITE);

                ChessPosition currPos = new ChessPosition(x + 1, y + 1);

                if (squaresToHighlight != null && squaresToHighlight.contains(currPos)){

                    out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                }

                printPieces(out, boardDisplay, x, y);
            }
    }

    @Override
    public void notify(ServerMessage message) throws NetworkException {
        try {
            switch (message.getServerMessageType()) {

                case LOAD_GAME -> {

                    this.game = message.getGame();

                    drawBoard(null);

                    System.out.print("\n>>> ");
                }
                case NOTIFICATION -> {
                    System.out.println(message.getMsg());

                    if (message.getMsg().contains("has resigned") || message.getMsg().contains("wins") ||
                            message.getMsg().contains("checkmate") || message.getMsg().contains("stalemate")) {

                        gameOver = true;
                    }
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
        return "You successfully left the game! \n";
    }

    public String move(String ... params) throws NetworkException {

        if (gameOver) {

            return "Game is already over";
        }

        if (teamColor == null){

            throw new NetworkException(500, "Observers can't make moves!");
        }

        if (!(params.length == 2 || params.length == 3)) {

            throw new NetworkException(500, "Please enter <startPos> <endPos> and optional promotion");
        }

        String startPos = params[0];
        String endPos = params[1];

        String promotion = null;

        ChessPiece.PieceType finalPromotion = null;

        if (params.length == 3){
            promotion = params[2];
        }

        if (game.getTeamTurn() != teamColor){

            throw new NetworkException(500, "Not Your turn!");
        }

        confirmPosInput(startPos);
        confirmPosInput(endPos);

        if (promotion != null){
            switch(promotion.toUpperCase()) {
                case "Q" -> finalPromotion = QUEEN;
                case "B" -> finalPromotion = BISHOP;
                case "K" -> finalPromotion = KNIGHT;
                case "R" -> finalPromotion = ROOK;
                default -> throw new NetworkException(500, "Only Q, B, K, and R are valid promotions");
            }
        }

        ChessMove move = lettersToNums(startPos, endPos, finalPromotion);
        ChessBoard board = game.getBoard();
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null){

            throw new NetworkException(500, "No Piece at this position");
        }

        if (piece.getTeamColor() != teamColor){

            throw new NetworkException(500, "Can't move opponent's piece");
        }
        Collection<ChessMove> moves = game.validMoves(move.getStartPosition());

        if (!moves.contains(move)) {

            throw new NetworkException(500, "Not a valid move");
        }
        ws.makeMove(authToken, gameID, playerColor, move);

        return "";
    }

    public String resign() throws NetworkException {
        if (gameOver) {

            return "Game is already over";
        }

        if (teamColor == null){

            help();
        }

        return "Are you sure you would like to resign? [Y/N]";
    }

    public String resignFinal() throws NetworkException {

        ws.resign(authToken, gameID, playerColor);

        return "Resignation successful \n";
    }

    public ChessMove lettersToNums(String startPos, String endPos, ChessPiece.PieceType promotion)
            throws NetworkException {
        String startCol = String.valueOf(startPos.charAt(0)).toUpperCase();
        int startRow = Character.getNumericValue(startPos.charAt(1));
        String endCol = String.valueOf(endPos.charAt(0)).toUpperCase();
        int endRow = Character.getNumericValue(endPos.charAt(1));

        ChessPosition moveStartPos = new ChessPosition(startRow, getColNum(startCol));

        ChessPosition moveEndPos = new ChessPosition(endRow, getColNum(endCol));

        return new ChessMove(moveStartPos, moveEndPos, promotion);
    }

    public int getColNum(String letter) throws NetworkException {
        return switch(letter) {
            case "A" ->  1;
            case "B" -> 2;
            case "C" -> 3;
            case "D" -> 4;
            case "E" -> 5;
            case "F" -> 6;
            case "G" -> 7;
            case "H" -> 8;
            default -> throw new NetworkException(500, "Invalid Move");
        };
    }

    public String highlight(String... params) throws NetworkException {

        if (gameOver) {

            return "Game is already over";
        }

        if (params.length != 1){

            throw new NetworkException(500, "Please enter piece position (e.i. a3)");
        }

        String position = params[0];
        confirmPosInput(position);
        int row = Character.getNumericValue(position.charAt(1));
        String col = String.valueOf(position.charAt(0));
        int colNumb = getColNum(col.toUpperCase());

        ChessPosition pos = new ChessPosition(row, colNumb);
        Collection<ChessMove> moves = game.validMoves(pos);
        ArrayList<ChessPosition> squaresToHighlight = new ArrayList<>();

        squaresToHighlight.add(pos);

        for (ChessMove move : moves) {

            squaresToHighlight.add(move.getEndPosition());
        }

        drawBoard(squaresToHighlight);
        return "";
    }

    public void confirmPosInput(String pos) throws NetworkException {

        String[] validLetters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        Integer[] validNums = {1, 2, 3, 4, 5, 6, 7, 8};

        ArrayList<String> letters = new ArrayList<>(Arrays.asList(validLetters));
        List<Integer> numbers = new ArrayList<>(Arrays.asList(validNums));

        if (!(pos.length() == 2)){

            throw new NetworkException(500, "Start Pos and End Pos must be exactly 2 characters");
        }
        if (!(letters.contains(String.valueOf(pos.charAt(0)).toUpperCase()))){

            throw new NetworkException(500, "First character in chess move must be a letter A-H");
        }

        if (!(numbers.contains(Character.getNumericValue(pos.charAt(1))))){

            throw new NetworkException(500, "Second character in chess move must be a number 1-8");
        }
    }
}