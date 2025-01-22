package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PawnMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        ChessPiece current = board.getPiece(startPosition);

        ArrayList<ChessMove> moves = new ArrayList<>();

        // Makes moves for pawns that WHITE
        if (current.getTeamColor() == WHITE) {

            // Sets possible position changes for WHITE pawns
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition twoAhead = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
            ChessPosition diagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition diagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);

            if (inbounds(twoAhead) && startPosition.getRow() == 2 && board.getPiece(oneAhead) == null && board.getPiece(twoAhead) == null) {
                moves.add(new ChessMove(startPosition, twoAhead, null));
            }

            if (inbounds(oneAhead) && board.getPiece(oneAhead) == null && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null && current.getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));
            }

            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null && current.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }

            if (startPosition.getRow() == 7 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(startPosition, oneAhead));
            }

            if (startPosition.getRow() == 7 && board.getPiece(diagonalLeft) != null && current.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalLeft));
            }

            if (startPosition.getRow() == 7 && board.getPiece(diagonalRight) != null && current.getTeamColor() != board.getPiece(diagonalRight).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalRight));
            }
        }

        // Makes moves for pawns that are BLACK
        if (current.getTeamColor() == BLACK) {

            // sets position changes for pawns that are BLACK
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            ChessPosition twoAhead = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn());
            ChessPosition diagonalRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            ChessPosition diagonalLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);

            if (inbounds(twoAhead) && startPosition.getRow() == 7 && board.getPiece(oneAhead) == null && board.getPiece(oneAhead) == null) {
                moves.add(new ChessMove(startPosition, twoAhead, null));
            }

            if (inbounds(oneAhead) && board.getPiece(oneAhead) == null && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null && current.getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));
            }

            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null && current.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }

            if (startPosition.getRow() == 2 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(startPosition, oneAhead));
            }

            if (startPosition.getRow() == 2 && board.getPiece(diagonalLeft) != null && current.getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalLeft));
            }

            if (startPosition.getRow() == 2 && board.getPiece(diagonalRight) != null && current.getTeamColor() != board.getPiece(diagonalRight).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalRight));
            }
        }

        return moves;
    }
}

