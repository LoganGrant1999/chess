package chess;

import chess.piecemoves.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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

        if (piece == PieceType.PAWN) {
            return new PawnMovesCalculator().calculateMoves(board, myPosition);
        }

        if (piece == PieceType.KING) {
            return new KingMovesCalculator().calculateMoves(board, myPosition);
        }

        if (piece == PieceType.KNIGHT) {
            return new KnightMovesCalculator().calculateMoves(board, myPosition);
        }

        if (piece == PieceType.ROOK) {
            return new RookMovesCalculator().calculateMoves(board, myPosition);
        }

        if (piece == PieceType.BISHOP) {
            return new BishopMovesCalculator().calculateMoves(board, myPosition);
        }

        if (piece == PieceType.QUEEN) {
            return new QueenMovesCalculator().calculateMoves(board, myPosition);
        }

        return new ArrayList<ChessMove>();
    }

    // Override methods to ensure no equals or hashCode issues

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

