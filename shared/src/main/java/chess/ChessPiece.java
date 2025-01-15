package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    private ChessGame.TeamColor teamColor;

    private  PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor(){
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }


    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition startPosition) {
        /** find current position
         * See if 1 move in front is open
         * Start can move 1 or 2 forward
         * see if diagonals are open
         * See if in diagonal is in bounds (make separate method)
         */

        ChessPiece current = board.getPiece(startPosition);

        ArrayList<ChessMove> moves = new ArrayList<>();

        if (current.getTeamColor() == WHITE) {
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition twoAhead = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
            ChessPosition diagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition diagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
            //This makes it possible for pawns to move 2 forward from starting position if there's nothing in front
            if (startPosition.getRow() == 2 &&  board.getPiece(oneAhead) == null &&  board.getPiece(twoAhead) == null){
                moves.add(new ChessMove(startPosition, twoAhead, null));
            }

            //This makes it possible to move one forward if it's empty and if it's inbounds
            if (board.getPiece(oneAhead) == null && inbounds(oneAhead)){
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (board.getPiece(diagonalRight) != null && inbounds(diagonalRight) && board.getPiece(diagonalRight).getTeamColor() == BLACK) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));

            }

            if (board.getPiece(diagonalLeft) != null && inbounds(diagonalLeft) && board.getPiece(diagonalLeft).getTeamColor() == BLACK) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }
        }




    }


    public boolean inbounds(ChessPosition endPosition) {
        if (endPosition.getRow() > 0 && endPosition.getRow() < 9 && endPosition.getColumn() > 0 && endPosition.getColumn() < 9){
            return true;
        }
        else {
            return false;
        }
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }
}

