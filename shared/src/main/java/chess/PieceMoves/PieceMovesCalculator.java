package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {

    public Collection<ChessMove> calculateMoves (ChessBoard board, ChessPosition pos);

    default boolean inbounds(ChessPosition pos) {
        if (pos.getRow() > 0 && pos.getRow() < 9 && pos.getColumn() > 0 && pos.getColumn() < 9) {
            return true;
        } else {
            return false;
        }
    }

    default ArrayList<ChessMove> promoter(ChessPosition startPos, ChessPosition endPos) {
        ArrayList<ChessMove> promotions = new ArrayList<>();
        promotions.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.QUEEN));
        promotions.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.BISHOP));
        promotions.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.KNIGHT));
        promotions.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.ROOK));
        return promotions;
    }

}





