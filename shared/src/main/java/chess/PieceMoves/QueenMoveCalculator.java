package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(new BishopMoveCalculator().calculateMoves(board, pos));

        moves.addAll(new RookMoveCalculator().calculateMoves(board, pos));

        return moves;
    }
}
