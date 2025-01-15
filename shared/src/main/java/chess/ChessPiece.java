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

    private PieceType pieceType;

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
    public ChessGame.TeamColor getTeamColor() {
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
        PieceType piece = getPieceType();

        if (piece == PieceType.PAWN){
            return pawnMoves(board, myPosition);
        }

        return new ArrayList<ChessMove>();
    }


    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition startPosition) {
        /** find current position
         * See if 1 move in front is open
         * Start can move 2 forward
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

            //This makes it possible for white pawns to move 2 forward from starting position if there's nothing in front
            if (startPosition.getRow() == 2 && board.getPiece(oneAhead) == null && board.getPiece(twoAhead) == null && inbounds(startPosition)) {
                moves.add(new ChessMove(startPosition, twoAhead, null));
            }

            //This makes it possible to move a white piece one forward if it's empty and if it's inbounds
            if (board.getPiece(oneAhead) == null && inbounds(oneAhead) && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            // This makes it possible to take a white piece diagonally right of yourself
            if (board.getPiece(diagonalRight) != null && inbounds(diagonalRight) && getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));

            }

            // This makes it possible to take a white piece diagonally left of yourself
            if (board.getPiece(diagonalLeft) != null && inbounds(diagonalLeft) && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }

            // This makes it possible to move a white peice one  ahead and get a promotion piece
            if (startPosition.getRow() == 7 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(startPosition, oneAhead));
            }

            // This makes it possible to take a white peice diagonally left and get a promotion piece
            if (startPosition.getRow() == 7 && board.getPiece(diagonalLeft) != null && board.getPiece(diagonalLeft).getTeamColor() == BLACK) {
                moves.addAll(promoter(startPosition, diagonalLeft));
            }

            // This makes it possible to take a  white piece diagonally right and get a promtion peice
            if (startPosition.getRow() == 7 && board.getPiece(diagonalRight) != null && board.getPiece(diagonalRight).getTeamColor() == BLACK) {
                moves.addAll(promoter(startPosition, diagonalRight));
            }
        }

        return moves;
    }


    public boolean inbounds(ChessPosition endPosition) {
        if (endPosition.getRow() > 0 && endPosition.getRow() < 9 && endPosition.getColumn() > 0 && endPosition.getColumn() < 9) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ChessMove> promoter(ChessPosition startPos, ChessPosition endPos) {
        ArrayList<ChessMove> promotions = new ArrayList<>();
        promotions.add(new ChessMove(startPos, endPos, PieceType.QUEEN));
        promotions.add(new ChessMove(startPos, endPos, PieceType.BISHOP));
        promotions.add(new ChessMove(startPos, endPos, PieceType.KNIGHT));
        promotions.add(new ChessMove(startPos, endPos, PieceType.ROOK));
        return promotions;
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

