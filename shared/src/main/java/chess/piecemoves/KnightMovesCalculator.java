package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;


public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {

        ChessPiece curr = board.getPiece(pos);

        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPosition forwardLongRight = new ChessPosition(pos.getRow() + 2, pos.getColumn() + 1);
        ChessPosition forwardLongLeft = new ChessPosition(pos.getRow() + 2, pos.getColumn() - 1);
        ChessPosition forwardShortRight = new ChessPosition(pos.getRow() + 1, pos.getColumn() + 2);
        ChessPosition forwardShortLeft = new ChessPosition(pos.getRow() + 1, pos.getColumn() - 2);
        ChessPosition backwardLongRight = new ChessPosition(pos.getRow() - 2, pos.getColumn() + 1);
        ChessPosition backwardLongLeft = new ChessPosition(pos.getRow() - 2, pos.getColumn() - 1);
        ChessPosition backwardShortRight = new ChessPosition(pos.getRow() - 1, pos.getColumn() + 2);
        ChessPosition backwardShortLeft = new ChessPosition(pos.getRow() - 1, pos.getColumn() - 2);

        if (inbounds(forwardLongRight) && (board.getPiece(forwardLongRight) == null
                || curr.getTeamColor() != board.getPiece(forwardLongRight).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardLongRight, null));
        }

        if (inbounds(forwardLongLeft) && (board.getPiece(forwardLongLeft) == null
                || curr.getTeamColor() != board.getPiece(forwardLongLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardLongLeft, null));
        }

        if (inbounds(forwardShortRight) && (board.getPiece(forwardShortRight) == null
                || curr.getTeamColor() != board.getPiece(forwardShortRight).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardShortRight, null));
        }

        if (inbounds(forwardShortLeft) && (board.getPiece(forwardShortLeft) == null
                || curr.getTeamColor() != board.getPiece(forwardShortLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardShortLeft, null));
        }

        if (inbounds(backwardLongRight) && (board.getPiece(backwardLongRight) == null
                || curr.getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardLongRight, null));
        }

        if (inbounds(backwardLongLeft) && (board.getPiece(backwardLongLeft) == null
                || curr.getTeamColor() != board.getPiece(backwardLongLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardLongLeft, null));
        }

        if (inbounds(backwardShortRight) && (board.getPiece(backwardShortRight) == null
                || curr.getTeamColor() != board.getPiece(backwardShortRight).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardShortRight, null));
        }

        if (inbounds(backwardShortLeft) && (board.getPiece(backwardShortLeft) == null
                || curr.getTeamColor() != board.getPiece(backwardShortLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardShortLeft, null));
        }

        return moves;
    }
}

