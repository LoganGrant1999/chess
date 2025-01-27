package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {
        ChessPiece curr = board.getPiece(pos);

        ArrayList<ChessMove> moves = new ArrayList<>();

        if (curr.getTeamColor() == WHITE) {

            ChessPosition oneAhead = new ChessPosition(pos.getRow() + 1, pos.getColumn());
            ChessPosition twoAhead = new ChessPosition(pos.getRow() + 2, pos.getColumn());
            ChessPosition diagonalRight = new ChessPosition(pos.getRow() + 1, pos.getColumn() + 1);
            ChessPosition diagonalLeft = new ChessPosition(pos.getRow() + 1, pos.getColumn() - 1);

            if (inbounds(twoAhead) && board.getPiece(oneAhead) == null && board.getPiece(twoAhead) == null && pos.getRow() == 2) {
                moves.add(new ChessMove(pos, twoAhead, null));
            }

            if (inbounds(oneAhead) && board.getPiece(oneAhead) == null && pos.getRow() != 7) {
                moves.add(new ChessMove(pos, oneAhead, null));
            }

            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null
                    && curr.getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && pos.getRow() != 7) {
                moves.add(new ChessMove(pos, diagonalRight, null));
            }

            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null &&
                    curr.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && pos.getRow() != 7) {
                moves.add(new ChessMove(pos, diagonalLeft, null));
            }

            if (pos.getRow() == 7 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(pos, oneAhead));
            }

            if (pos.getRow() == 7 && board.getPiece(diagonalRight) != null
                    && curr.getTeamColor() != board.getPiece(diagonalRight).getTeamColor()) {
                moves.addAll(promoter(pos, diagonalRight));
            }

            if (pos.getRow() == 7 && board.getPiece(diagonalLeft) != null
                    && curr.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(pos, diagonalLeft));
            }
        }

        if (curr.getTeamColor() == BLACK) {

            ChessPosition oneAhead = new ChessPosition(pos.getRow() - 1, pos.getColumn());
            ChessPosition twoAhead = new ChessPosition(pos.getRow() - 2, pos.getColumn());
            ChessPosition diagonalRight = new ChessPosition(pos.getRow() - 1, pos.getColumn() + 1);
            ChessPosition diagonalLeft = new ChessPosition(pos.getRow() - 1, pos.getColumn() - 1);

            if (inbounds(twoAhead) && pos.getRow() == 7 && board.getPiece(oneAhead) == null && board.getPiece(twoAhead) == null) {
                moves.add(new ChessMove(pos, twoAhead, null));
            }

            if (inbounds(oneAhead) && board.getPiece(oneAhead) == null && pos.getRow() != 2) {
                moves.add(new ChessMove(pos, oneAhead, null));
            }

            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null
                    && curr.getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && pos.getRow() != 2) {
                moves.add(new ChessMove(pos, diagonalRight, null));
            }

            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null
                    && curr.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && pos.getRow() != 2) {
                moves.add(new ChessMove(pos, diagonalLeft, null));
            }

            if (pos.getRow() == 2 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(pos, oneAhead));
            }

            if (pos.getRow() == 2 && board.getPiece(diagonalLeft) != null && curr.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(pos, diagonalLeft));
            }

            if (pos.getRow() == 2 && board.getPiece(diagonalRight) != null && curr.getTeamColor() != board.getPiece(diagonalRight).getTeamColor()) {
                moves.addAll(promoter(pos, diagonalRight));
            }
        }

        return moves;
    }
}