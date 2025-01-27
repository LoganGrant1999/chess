package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece curr = board.getPiece(pos);

        // Going Up and Right
        for (int i = 1; i < 9; i++) {
            ChessPosition upAndRight = new ChessPosition(pos.getRow() + i, pos.getColumn() + i);

            if (!inbounds(upAndRight) || (board.getPiece(upAndRight) != null
                    && curr.getTeamColor() == board.getPiece(upAndRight).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(pos, upAndRight, null));

            if (board.getPiece(upAndRight) != null
                    && curr.getTeamColor() != board.getPiece(upAndRight).getTeamColor()) {
                break;
            }
        }

        // Going Up and Left
        for (int i = 1; i < 9; i++) {
            ChessPosition upAndLeft = new ChessPosition(pos.getRow() + i, pos.getColumn() - i);

            if (!inbounds(upAndLeft) || (board.getPiece(upAndLeft) != null
                    && curr.getTeamColor() == board.getPiece(upAndLeft).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(pos, upAndLeft, null));

            if (board.getPiece(upAndLeft) != null
                    && curr.getTeamColor() != board.getPiece(upAndLeft).getTeamColor()) {
                break;
            }
        }

        //Going Down and right
        for (int i = 1; i < 9; i++) {
            ChessPosition downAndRight = new ChessPosition(pos.getRow() - i, pos.getColumn() + i);

            if (!inbounds(downAndRight) || (board.getPiece(downAndRight) != null
                    && curr.getTeamColor() == board.getPiece(downAndRight).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(pos, downAndRight, null));

            if (board.getPiece(downAndRight) != null
                    && curr.getTeamColor() != board.getPiece(downAndRight).getTeamColor()) {
                break;
            }
        }

        // Going Down and Left
        for (int i = 1; i < 9; i++) {
            ChessPosition downAndLeft = new ChessPosition(pos.getRow() - i, pos.getColumn() - i);

            if (!inbounds(downAndLeft) || (board.getPiece(downAndLeft) != null
                    && curr.getTeamColor() == board.getPiece(downAndLeft).getTeamColor())) {
                break;
            }

            moves.add(new ChessMove(pos, (downAndLeft), null));

            if (board.getPiece(downAndLeft) != null
                    && curr.getTeamColor() != board.getPiece(downAndLeft).getTeamColor()) {
                break;
            }
        }

        return moves;
    }
}

