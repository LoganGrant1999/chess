package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPos) {

            ChessPiece current = board.getPiece(startPos);

            ArrayList<ChessMove> moves = new ArrayList<>();

            // sets moves for WHITE KNIGHTS
            if (current.getTeamColor() == WHITE) {
                ChessPosition forwardLongRight = new ChessPosition(startPos.getRow() + 2, startPos.getColumn() + 1);
                ChessPosition forwardLongLeft = new ChessPosition(startPos.getRow() + 2, startPos.getColumn() - 1);
                ChessPosition forwardShortRight = new ChessPosition(startPos.getRow() + 1, startPos.getColumn() + 2);
                ChessPosition forwardShortLeft = new ChessPosition(startPos.getRow() + 1, startPos.getColumn() - 2);
                ChessPosition backwardLongRight = new ChessPosition(startPos.getRow() - 2, startPos.getColumn() + 1);
                ChessPosition backwardLongLeft = new ChessPosition(startPos.getRow() - 2, startPos.getColumn() - 1);
                ChessPosition backwardShortRight = new ChessPosition(startPos.getRow() - 1, startPos.getColumn() + 2);
                ChessPosition backwardShortLeft = new ChessPosition(startPos.getRow() - 1, startPos.getColumn() - 2);

                if (inbounds(forwardLongRight) && (board.getPiece(forwardLongRight) == null || current.getTeamColor() != board.getPiece(forwardLongRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardLongRight, null));
                }

                if (inbounds(forwardLongLeft) && (board.getPiece(forwardLongLeft) == null || current.getTeamColor() != board.getPiece(forwardLongLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardLongLeft, null));
                }

                if (inbounds(forwardShortRight) && (board.getPiece(forwardShortRight) == null || current.getTeamColor() != board.getPiece(forwardShortRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardShortRight, null));
                }

                if (inbounds(forwardShortLeft) && (board.getPiece(forwardShortLeft) == null || current.getTeamColor() != board.getPiece(forwardShortLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardShortLeft, null));
                }

                if (inbounds(backwardLongRight) && (board.getPiece(backwardLongRight) == null || current.getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardLongRight, null));
                }

                if (inbounds(backwardLongLeft) && (board.getPiece(backwardLongLeft) == null || current.getTeamColor() != board.getPiece(backwardLongLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardLongLeft, null));
                }

                if (inbounds(backwardShortRight) && (board.getPiece(backwardShortRight) == null || current.getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardShortRight, null));
                }

                if (inbounds(backwardShortLeft) && (board.getPiece(backwardShortLeft) == null || current.getTeamColor() != board.getPiece(backwardShortLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardShortLeft, null));
                }
            }

            // sets moves for BLACK KNIGHTS
            if (current.getTeamColor() == BLACK) {
                ChessPosition forwardLongRight = new ChessPosition(startPos.getRow() - 2, startPos.getColumn() + 1);
                ChessPosition forwardLongLeft = new ChessPosition(startPos.getRow() - 2, startPos.getColumn() - 1);
                ChessPosition forwardShortRight = new ChessPosition(startPos.getRow() - 1, startPos.getColumn() + 2);
                ChessPosition forwardShortLeft = new ChessPosition(startPos.getRow() - 1, startPos.getColumn() - 2);
                ChessPosition backwardLongRight = new ChessPosition(startPos.getRow() + 2, startPos.getColumn() + 1);
                ChessPosition backwardLongLeft = new ChessPosition(startPos.getRow() + 2, startPos.getColumn() - 1);
                ChessPosition backwardShortRight = new ChessPosition(startPos.getRow() + 1, startPos.getColumn() + 2);
                ChessPosition backwardShortLeft = new ChessPosition(startPos.getRow() + 1, startPos.getColumn() - 2);

                if (inbounds(forwardLongRight) && (board.getPiece(forwardLongRight) == null || current.getTeamColor() != board.getPiece(forwardLongRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardLongRight, null));
                }

                if (inbounds(forwardLongLeft) && (board.getPiece(forwardLongLeft) == null || current.getTeamColor() != board.getPiece(forwardLongLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardLongLeft, null));
                }

                if (inbounds(forwardShortRight) && (board.getPiece(forwardShortRight) == null || current.getTeamColor() != board.getPiece(forwardShortRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardShortRight, null));
                }

                if (inbounds(forwardShortLeft) && (board.getPiece(forwardShortLeft) == null || current.getTeamColor() != board.getPiece(forwardShortLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, forwardShortLeft, null));
                }

                if (inbounds(backwardLongRight) && (board.getPiece(backwardLongRight) == null || current.getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardLongRight, null));
                }

                if (inbounds(backwardLongLeft) && (board.getPiece(backwardLongLeft) == null || current.getTeamColor() != board.getPiece(backwardLongLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardLongLeft, null));
                }

                if (inbounds(backwardShortRight) && (board.getPiece(backwardShortRight) == null || current.getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardShortRight, null));
                }

                if (inbounds(backwardShortLeft) && (board.getPiece(backwardShortLeft) == null || current.getTeamColor() != board.getPiece(backwardShortLeft).getTeamColor())) {
                    moves.add(new ChessMove(startPos, backwardShortLeft, null));
                }
            }

            return moves;
    }
}

