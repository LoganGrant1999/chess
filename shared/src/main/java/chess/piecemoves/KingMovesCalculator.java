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
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        ChessPiece current = board.getPiece(startPosition);

        ArrayList<ChessMove> moves = new ArrayList<>();

        // sets moves for the WHITE KING
        if (current.getTeamColor() == WHITE) {
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition oneBehind = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
            ChessPosition backwardDiagonalRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            ChessPosition backwardDiagonalLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);

            if (inbounds(oneAhead) && (board.getPiece(oneAhead) == null || current.getTeamColor() != board.getPiece(oneAhead).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (inbounds(oneBehind) && (board.getPiece(oneBehind) == null || current.getTeamColor() != board.getPiece(oneBehind).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneBehind, null));
            }

            if (inbounds(oneRight) && (board.getPiece(oneRight) == null || current.getTeamColor() != board.getPiece(oneRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneRight, null));
            }

            if (inbounds(oneLeft) && (board.getPiece(oneLeft) == null || current.getTeamColor() != board.getPiece(oneLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneLeft, null));
            }

            if (inbounds(forwardDiagonalRight) && (board.getPiece(forwardDiagonalRight) == null || current.getTeamColor() != board.getPiece(forwardDiagonalRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardDiagonalRight, null));
            }

            if (inbounds(forwardDiagonalLeft) && (board.getPiece(forwardDiagonalLeft) == null || current.getTeamColor() != board.getPiece(forwardDiagonalLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardDiagonalLeft, null));
            }

            if (inbounds(backwardDiagonalRight) && (board.getPiece(backwardDiagonalRight) == null || current.getTeamColor() != board.getPiece(backwardDiagonalRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardDiagonalRight, null));
            }

            if (inbounds(backwardDiagonalLeft) && (board.getPiece(backwardDiagonalLeft) == null || current.getTeamColor() != board.getPiece(backwardDiagonalLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardDiagonalLeft, null));
            }
        }

        // sets moves for the BLACK KING
        if (current.getTeamColor() == BLACK) {
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            ChessPosition oneBehind = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            ChessPosition backwardDiagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition backwardDiagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);

            if (inbounds(oneAhead) && (board.getPiece(oneAhead) == null || current.getTeamColor() != board.getPiece(oneAhead).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (inbounds(oneBehind) && (board.getPiece(oneBehind) == null || current.getTeamColor() != board.getPiece(oneBehind).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneBehind, null));
            }

            if (inbounds(oneRight) && (board.getPiece(oneRight) == null || current.getTeamColor() != board.getPiece(oneRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneRight, null));
            }

            if (inbounds(oneLeft) && (board.getPiece(oneLeft) == null || current.getTeamColor() != board.getPiece(oneLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneLeft, null));
            }

            if (inbounds(forwardDiagonalRight) && (board.getPiece(forwardDiagonalRight) == null || current.getTeamColor() != board.getPiece(forwardDiagonalRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardDiagonalRight, null));
            }

            if (inbounds(forwardDiagonalLeft) && (board.getPiece(forwardDiagonalLeft) == null || current.getTeamColor() != board.getPiece(forwardDiagonalLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardDiagonalLeft, null));
            }

            if (inbounds(backwardDiagonalRight) && (board.getPiece(backwardDiagonalRight) == null || current.getTeamColor() != board.getPiece(backwardDiagonalRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardDiagonalRight, null));
            }

            if (inbounds(backwardDiagonalLeft) && (board.getPiece(backwardDiagonalLeft) == null || current.getTeamColor() != board.getPiece(backwardDiagonalLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardDiagonalLeft, null));
            }
        }

        return moves;
    }
}
