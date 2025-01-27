package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class KingMovesCalculator implements PieceMovesCalculator {


    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {
        ChessPiece curr = board.getPiece(pos);

        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPosition oneAhead = new ChessPosition(pos.getRow() + 1, pos.getColumn());
        ChessPosition oneBehind = new ChessPosition(pos.getRow() - 1, pos.getColumn());
        ChessPosition oneLeft = new ChessPosition(pos.getRow(), pos.getColumn() - 1);
        ChessPosition oneRight = new ChessPosition(pos.getRow(), pos.getColumn() + 1);
        ChessPosition forwardDiagonalRight = new ChessPosition(pos.getRow() + 1, pos.getColumn() + 1);
        ChessPosition forwardDiagonalLeft = new ChessPosition(pos.getRow() + 1, pos.getColumn() - 1);
        ChessPosition backwardDiagonalRight = new ChessPosition(pos.getRow() - 1, pos.getColumn() + 1);
        ChessPosition backwardDiagonalLeft = new ChessPosition(pos.getRow() - 1, pos.getColumn() - 1);

        if (inbounds(oneAhead) && (board.getPiece(oneAhead) == null
                || curr.getTeamColor() != board.getPiece(oneAhead).getTeamColor())) {
            moves.add(new ChessMove(pos, oneAhead, null));
        }

        if (inbounds(oneBehind) && (board.getPiece(oneBehind) == null
                || curr.getTeamColor() != board.getPiece(oneBehind).getTeamColor())) {
            moves.add(new ChessMove(pos, oneBehind, null));
        }

        if (inbounds(oneRight) && (board.getPiece(oneRight) == null
                || curr.getTeamColor() != board.getPiece(oneRight).getTeamColor())) {
            moves.add(new ChessMove(pos, oneRight, null));
        }

        if (inbounds(oneLeft) && (board.getPiece(oneLeft) == null
                || curr.getTeamColor() != board.getPiece(oneLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, oneLeft, null));
        }

        if (inbounds(forwardDiagonalRight) && (board.getPiece(forwardDiagonalRight) == null
                || curr.getTeamColor() != board.getPiece(forwardDiagonalRight).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardDiagonalRight, null));
        }

        if (inbounds(forwardDiagonalLeft) && (board.getPiece(forwardDiagonalLeft) == null
                || curr.getTeamColor() != board.getPiece(forwardDiagonalLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardDiagonalLeft, null));
        }

        if (inbounds(backwardDiagonalRight) && (board.getPiece(backwardDiagonalRight) == null
                || curr.getTeamColor() != board.getPiece(backwardDiagonalRight).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardDiagonalRight, null));
        }

        if (inbounds(backwardDiagonalLeft) && (board.getPiece(backwardDiagonalLeft) == null
                || curr.getTeamColor() != board.getPiece(backwardDiagonalLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardDiagonalLeft, null));
        }

        return moves;
    }
}
