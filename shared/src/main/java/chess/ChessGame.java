package chess;

import chess.piecemoves.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    public ChessGame(ChessGame oldGame) {
        this.board = new ChessBoard(oldGame.board);
        this.teamTurn = oldGame.teamTurn;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        ChessPiece startPiece = board.getPiece(startPosition);

        if (board.getPiece(startPosition) == null){
            return null;
        }

        Collection<ChessMove> moves = startPiece.pieceMoves(board, startPosition);

        for (ChessMove move : moves){

            ChessGame copyGame = new ChessGame(this);

            copyGame.getBoard().addPiece(move.getEndPosition(), startPiece);
            copyGame.getBoard().addPiece(move.getStartPosition(), null);

            if (!copyGame.isInCheck(startPiece.getTeamColor())) {
                goodMoves.add(move);
            }
        }

        return goodMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece.PieceType promote = move.getPromotionPiece();
        ChessPiece currPiece = board.getPiece(startPos);

        if (board.getPiece(startPos) == null
                || board.getPiece(startPos).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException();
        }

        Collection<ChessMove> moves = validMoves(startPos);

        if (!moves.contains(move)){
            throw new InvalidMoveException();
        }

        if (promote == null){
            board.addPiece(endPos, currPiece);
        } else {
            ChessPiece promotePiece = new ChessPiece(teamTurn, promote);
            board.addPiece(endPos, promotePiece);
        }

        board.addPiece(startPos, null);

        if (teamTurn == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        } else{
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition currKing = kingPos(teamColor);
        ArrayList<ChessMove> moves = new ArrayList<>();

        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {

                ChessPosition currPos = new ChessPosition(x, y);
                ChessPiece currPiece = board.getPiece(currPos);

                if (currPiece != null && currPiece.getTeamColor() != teamColor){
                    moves.addAll(currPiece.pieceMoves(board, currPos));
                }
            }
        }

        for (ChessMove move: moves) {
            if (move.getEndPosition().equals(currKing)) {
                return true;
            }
        }
        return false;
    }


    public ChessPosition kingPos(TeamColor color) {

        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {

                ChessPosition curr = new ChessPosition(x, y);
                ChessPiece currPiece = board.getPiece(curr);

                if (currPiece != null && currPiece.getPieceType() == ChessPiece.PieceType.KING
                        && currPiece.getTeamColor() == color) {
                    return curr;
                }
            }
        }

        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    public boolean isInCheckmate(TeamColor teamColor) {

        ArrayList<ChessMove> currTeamMoves = new ArrayList<>();

        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {

                ChessPosition currPos = new ChessPosition(x, y);
                ChessPiece currPiece = board.getPiece(currPos);

                if (currPiece != null && currPiece.getTeamColor() == teamColor) {

                    Collection<ChessMove> moves = currPiece.pieceMoves(board, currPos);
                    currTeamMoves.addAll(moves);
                }
            }
        }

        for (ChessMove move : currTeamMoves){

            ChessGame copyGame = new ChessGame(this);
            ChessPiece movingPiece = copyGame.getBoard().getPiece(move.getStartPosition());
            copyGame.getBoard().addPiece(move.getEndPosition(), movingPiece);
            copyGame.getBoard().addPiece(move.getStartPosition(), null);

            if (!copyGame.isInCheck(teamColor)){
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return false;
        }

        return isInCheckmate(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }
}