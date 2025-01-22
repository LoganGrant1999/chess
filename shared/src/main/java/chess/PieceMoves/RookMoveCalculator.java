package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece curr = board.getPiece(startPosition);

        // Going down
        for (int i = 1; i < 9; i++) {

            ChessPosition oneDown = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn());

            if (!inbounds(oneDown) || (board.getPiece(oneDown) != null && curr.getTeamColor() == board.getPiece(oneDown).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(startPosition, oneDown, null));

            if (board.getPiece(oneDown) != null && curr.getTeamColor() != board.getPiece(oneDown).getTeamColor()) {
                break;
            }
        }

        // Going up
        for (int i = 1; i < 9; i++) {

            ChessPosition oneUp = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());

            if (!inbounds(oneUp) || (board.getPiece(oneUp) != null && curr.getTeamColor() == board.getPiece(oneUp).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(startPosition, oneUp, null));

            if (board.getPiece(oneUp) != null && curr.getTeamColor() != board.getPiece(oneUp).getTeamColor()) {
                break;
            }
        }

        // Going right
        for (int i = 1; i < 9; i++) {
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);


            if (!inbounds(oneRight) || board.getPiece(oneRight) != null && curr.getTeamColor() == board.getPiece(oneRight).getTeamColor()) {
                break;
            }

            moves.add(new ChessMove(startPosition, oneRight, null));

            if (board.getPiece(oneRight) != null && curr.getTeamColor() != board.getPiece(oneRight).getTeamColor()) {
                break;
            }
        }

        //Going Left
        for (int i = 1; i < 9; i++) {
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i);
            if (!inbounds(oneLeft) || board.getPiece(oneLeft) != null && curr.getTeamColor() == board.getPiece(oneLeft).getTeamColor()) {
                break;
            }

            moves.add(new ChessMove(startPosition, oneLeft, null));

            if (board.getPiece(oneLeft) != null && curr.getTeamColor() != board.getPiece(oneLeft).getTeamColor()) {
                break;
            }
        }

        return moves;

    }
}

