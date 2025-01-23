package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(new BishopMovesCalculator().calculateMoves(board, pos));

        moves.addAll(new RookMovesCalculator().calculateMoves(board, pos));

        return moves;
    }
}
