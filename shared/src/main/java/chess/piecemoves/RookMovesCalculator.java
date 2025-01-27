package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece curr = board.getPiece(pos);

        // Going down
        for (int i = 1; i < 9; i++) {

            ChessPosition oneDown = new ChessPosition(pos.getRow() + i, pos.getColumn());

            if (!inbounds(oneDown) || (board.getPiece(oneDown) != null
                    && curr.getTeamColor() == board.getPiece(oneDown).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(pos, oneDown, null));

            if (board.getPiece(oneDown) != null
                    && curr.getTeamColor() != board.getPiece(oneDown).getTeamColor()) {
                break;
            }
        }

        // Going up
        for (int i = 1; i < 9; i++) {

            ChessPosition oneUp = new ChessPosition(pos.getRow() - i, pos.getColumn());

            if (!inbounds(oneUp) || (board.getPiece(oneUp) != null
                    && curr.getTeamColor() == board.getPiece(oneUp).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(pos, oneUp, null));

            if (board.getPiece(oneUp) != null
                    && curr.getTeamColor() != board.getPiece(oneUp).getTeamColor()) {
                break;
            }
        }

        // Going right
        for (int i = 1; i < 9; i++) {

            ChessPosition oneRight = new ChessPosition(pos.getRow(), pos.getColumn() + i);


            if (!inbounds(oneRight) || board.getPiece(oneRight) != null
                    && curr.getTeamColor() == board.getPiece(oneRight).getTeamColor()) {
                break;
            }

            moves.add(new ChessMove(pos, oneRight, null));

            if (board.getPiece(oneRight) != null
                    && curr.getTeamColor() != board.getPiece(oneRight).getTeamColor()) {
                break;
            }
        }

        //Going Left
        for (int i = 1; i < 9; i++) {

            ChessPosition oneLeft = new ChessPosition(pos.getRow(), pos.getColumn() - i);

            if (!inbounds(oneLeft) || board.getPiece(oneLeft) != null
                    && curr.getTeamColor() == board.getPiece(oneLeft).getTeamColor()) {
                break;
            }

            moves.add(new ChessMove(pos, oneLeft, null));

            if (board.getPiece(oneLeft) != null
                    && curr.getTeamColor() != board.getPiece(oneLeft).getTeamColor()) {
                break;
            }
        }

        return moves;
    }
}

