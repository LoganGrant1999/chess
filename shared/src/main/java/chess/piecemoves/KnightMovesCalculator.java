package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;


public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override

    /* Method creates all possible ChessMove objects for a piece with type KNIGHT and adds them to a collection */

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

        //Moves KNIGHT up 2 spaces and right one if WHITE, and back 2 spaces and right one if BLACK
        if (inbounds(forwardLongRight) && (board.getPiece(forwardLongRight) == null
                || curr.getTeamColor() != board.getPiece(forwardLongRight).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardLongRight, null));
        }

        //Moves KNIGHT up 2 spaces and left one if WHITE, and back 2 spaces and left one if BLACK
        if (inbounds(forwardLongLeft) && (board.getPiece(forwardLongLeft) == null
                || curr.getTeamColor() != board.getPiece(forwardLongLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardLongLeft, null));
        }

        //Moves KNIGHT up 1 space and right 2 spaces if WHITE, and  back 1 space and right 2 spaces if BLACK
        if (inbounds(forwardShortRight) && (board.getPiece(forwardShortRight) == null
                || curr.getTeamColor() != board.getPiece(forwardShortRight).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardShortRight, null));
        }

        //Moves KNIGHT up 1 space and left 2 spaces if WHITE, and back 1 space and left 2 spaces if BLACK
        if (inbounds(forwardShortLeft) && (board.getPiece(forwardShortLeft) == null
                || curr.getTeamColor() != board.getPiece(forwardShortLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, forwardShortLeft, null));
        }

        //Moves KNIGHT back 2 spaces and right one if WHITE, and up 2 spaces and right one if BLACK
        if (inbounds(backwardLongRight) && (board.getPiece(backwardLongRight) == null
                || curr.getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardLongRight, null));
        }

        //Moves KNIGHT back 2 spaces and left one if WHITE, and up 2 spaces and left one if BLACK
        if (inbounds(backwardLongLeft) && (board.getPiece(backwardLongLeft) == null
                || curr.getTeamColor() != board.getPiece(backwardLongLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardLongLeft, null));
        }

        //Moves KNIGHT back 1 space and right 2 spaces if WHITE, and up 1 space and right 2 spaces if BLACK
        if (inbounds(backwardShortRight) && (board.getPiece(backwardShortRight) == null
                || curr.getTeamColor() != board.getPiece(backwardShortRight).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardShortRight, null));
        }

        //Moves Knight back 1 space and left 2 spaces if WHITE, and up 1 space and left 2 spaces if BLACK
        if (inbounds(backwardShortLeft) && (board.getPiece(backwardShortLeft) == null
                || curr.getTeamColor() != board.getPiece(backwardShortLeft).getTeamColor())) {
            moves.add(new ChessMove(pos, backwardShortLeft, null));
        }

        return moves;
    }
}

