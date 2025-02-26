package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

/*This interface makes it so that all piecemoves classes inherit the inbounds method and promoter method so that
it is easier to  check if a piece is/will be inbounds with a move and if pawns need to be
promoted to other piece types*/

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





