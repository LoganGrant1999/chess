package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    /** Method creates all possible ChessMove objects for a piece with type QUEEN and adds them to a collection */

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        // Adds all BISHOP moves to QUEEN moves because QUEEN can move like BISHOP
        moves.addAll(new BishopMovesCalculator().calculateMoves(board, pos));

        // Adds all ROOK moves to QUEEN moves because QUEEN can move like ROOK
        moves.addAll(new RookMovesCalculator().calculateMoves(board, pos));

        return moves;
    }
}
