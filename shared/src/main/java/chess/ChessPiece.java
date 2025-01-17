package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    private ChessGame.TeamColor teamColor;

    private PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceType piece = getPieceType();

        if (piece == PieceType.PAWN){
            return pawnMoves(board, myPosition);
        }

        if (piece == PieceType.KING){
            return kingMoves(board, myPosition);
        }


        return new ArrayList<ChessMove>();
    }



    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition startPosition) {
        /**
         * find current position
         * Move King one space forward if in bounds and not occupied by piece of same color
         * Move King one space backward if in bounds and not occupied by piece of same color
         * Move King one space left if in bounds and not occupied by piece of same color
         * Move King one space right if in bounds and not occupied by piece of same color
         * Move King one space diagonally right and forward if in bounds and not occupied by piece of same color
         * Move King one space diagonally left and forward if in bounds and not occupied by piece of same color
         * Move King one space diagonally right and backwards if in bounds and not occupied by piece of same color
         * Move King one space diagonally left and backwards if in bounds and not occupied by piece of same color
         */

        ChessPiece current = board.getPiece(startPosition);

        ArrayList<ChessMove> moves = new ArrayList<>();

        // sets moves for the WHITE KING
        if (current.getTeamColor() == WHITE){

            // sets position changes for the WHITE KING
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition oneBehind = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
            ChessPosition ForwardDiagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition ForwardDiagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
            ChessPosition BackwardDiagonalRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            ChessPosition BackwardDiagonalLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);


            // Moves WHITE KING one space forward
            if (inbounds(oneAhead) && (board.getPiece(oneAhead) == null || getTeamColor() != board.getPiece(oneAhead).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            // Moves WHITE KING one space backward
            if (inbounds(oneBehind) && (board.getPiece(oneBehind) == null || getTeamColor() != board.getPiece(oneBehind).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneBehind, null));
            }

            // Moves WHITE KING one space right
            if (inbounds(oneRight) && (board.getPiece(oneRight) == null || getTeamColor() != board.getPiece(oneRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneRight, null));
            }

            // Moves WHITE KING one space left
            if (inbounds(oneLeft) && (board.getPiece(oneLeft) == null || getTeamColor() != board.getPiece(oneLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneLeft, null));
            }

            // Moves WHITE KING one space diagonally forward to the right
            if (inbounds(ForwardDiagonalRight) && (board.getPiece(ForwardDiagonalRight) == null || getTeamColor() != board.getPiece(ForwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, ForwardDiagonalRight, null));
            }


            // Moves WHITE KING one space diagonally forward to the left
            if (inbounds(ForwardDiagonalLeft) && (board.getPiece(ForwardDiagonalLeft) == null || getTeamColor() != board.getPiece(ForwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, ForwardDiagonalLeft, null));
            }

            // Moves WHITE KING one space diagonally backward to the right
            if (inbounds(BackwardDiagonalRight) && (board.getPiece(BackwardDiagonalRight) == null || getTeamColor() != board.getPiece(BackwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, BackwardDiagonalRight, null));
            }

            // Moves WHITE KING one space diagonally backward to the left
            if (inbounds(BackwardDiagonalLeft) && (board.getPiece(BackwardDiagonalLeft) == null || getTeamColor() != board.getPiece(BackwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, BackwardDiagonalLeft, null));
            }

        }

        // sets moves for the BLACK KING
        if (current.getTeamColor() == BLACK){

            // sets position changes for the WHITE KING
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            ChessPosition oneBehind = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
            ChessPosition ForwardDiagonalRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            ChessPosition ForwardDiagonalLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            ChessPosition BackwardDiagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition BackwardDiagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);

            // Moves BLACK KING one space forward
            if (inbounds(oneAhead) && (board.getPiece(oneAhead) == null || getTeamColor() != board.getPiece(oneAhead).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            // Moves BLACK KING one space backward
            if (inbounds(oneBehind) && (board.getPiece(oneBehind) == null || getTeamColor() != board.getPiece(oneBehind).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneBehind, null));
            }

            // Moves BLACK KING one space right
            if (inbounds(oneRight) && (board.getPiece(oneRight) == null || getTeamColor() != board.getPiece(oneRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneRight, null));
            }

            // Moves BLACK KING one space left
            if (inbounds(oneLeft) && (board.getPiece(oneLeft) == null || getTeamColor() != board.getPiece(oneLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneLeft, null));
            }

            // Moves BLACK KING one space diagonally forward to the right
            if (inbounds(ForwardDiagonalRight) && (board.getPiece(ForwardDiagonalRight) == null || getTeamColor() != board.getPiece(ForwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, ForwardDiagonalRight, null));
            }


            // Moves BLACK KING one space diagonally forward to the left
            if (inbounds(ForwardDiagonalLeft) && (board.getPiece(ForwardDiagonalLeft) == null || getTeamColor() != board.getPiece(ForwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, ForwardDiagonalLeft, null));
            }

            // Moves BLACK KING one space diagonally backward to the right
            if (inbounds(BackwardDiagonalRight) && (board.getPiece(BackwardDiagonalRight) == null || getTeamColor() != board.getPiece(BackwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, BackwardDiagonalRight, null));
            }

            // Moves BLACK KING one space diagonally backward to the left
            if (inbounds(BackwardDiagonalLeft) && (board.getPiece(BackwardDiagonalLeft) == null || getTeamColor() != board.getPiece(BackwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, BackwardDiagonalLeft, null));
            }

        }

        return moves;
    }





    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition startPosition) {
        /**
         *  find current position
         * See if 1 move in front is open
         * Start can move 2 forward
         * see if diagonals are open
         * See if in diagonal is in bounds (make separate method)
         */

        ChessPiece current = board.getPiece(startPosition);

        ArrayList<ChessMove> moves = new ArrayList<>();


        // Makes moves for pawns that WHITE
        if (current.getTeamColor() == WHITE) {

            // Sets possible position changes for WHITE pawns
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition twoAhead = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
            ChessPosition diagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition diagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);

            //This makes it possible for white pawns to move 2 forward from starting position if there's nothing in front
            if (inbounds(twoAhead) && startPosition.getRow() == 2 && board.getPiece(oneAhead) == null && board.getPiece(twoAhead) == null) {
                moves.add(new ChessMove(startPosition, twoAhead, null));
            }

            //This makes it possible to move a white piece one forward if it's empty and if it's inbounds
            if (inbounds(oneAhead) && board.getPiece(oneAhead) == null && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            // This makes it possible to take a white piece diagonally right of yourself
            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null && getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));

            }

            // This makes it possible to take a white piece diagonally left of yourself
            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }

            // This makes it possible to move a white peice one  ahead and get a promotion piece
            if (startPosition.getRow() == 7 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(startPosition, oneAhead));
            }

            // This makes it possible to take a white peice diagonally left and get a promotion piece
            if (startPosition.getRow() == 7 && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalLeft));
            }

            // This makes it possible to take a  white piece diagonally right and get a promotion peice
            if (startPosition.getRow() == 7 && board.getPiece(diagonalRight) != null && getTeamColor() != board.getPiece(diagonalRight).getTeamColor()) {
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

            //This makes it possible for black pawns to move 2 forward from starting position if there's nothing in front
            if (inbounds(twoAhead) && startPosition.getRow() == 7 && board.getPiece(oneAhead) == null && board.getPiece(oneAhead) == null) {
                moves.add(new ChessMove(startPosition, twoAhead, null));
            }

            //This makes it possible to move a black pawn one forward if it's empty and if it's inbounds
            if (inbounds(oneAhead) && board.getPiece(oneAhead) == null && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            // This makes it possible to take a black piece diagonally right of yourself
            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null && getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));
            }

            // This makes it possible to take a black piece diagonally left of yourself
            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }

            // This makes it possible to move a black pawn one ahead and get a promotion piece
            if (startPosition.getRow() == 2 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(startPosition, oneAhead));
            }


            // This makes it possible to take a black piece diagonally left and get a promotion piece
            if (startPosition.getRow() == 2 && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalLeft));
            }

            // This makes it possible to take a black piece diagonally right and get a promotion piece
            if (startPosition.getRow() == 2 && board.getPiece(diagonalRight) != null && getTeamColor() != board.getPiece(diagonalRight).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalRight));
            }
        }

        return moves;

    }


    public boolean inbounds(ChessPosition endPosition) {
        if (endPosition.getRow() > 0 && endPosition.getRow() < 9 && endPosition.getColumn() > 0 && endPosition.getColumn() < 9) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ChessMove> promoter(ChessPosition startPos, ChessPosition endPos) {
        ArrayList<ChessMove> promotions = new ArrayList<>();
        promotions.add(new ChessMove(startPos, endPos, PieceType.QUEEN));
        promotions.add(new ChessMove(startPos, endPos, PieceType.BISHOP));
        promotions.add(new ChessMove(startPos, endPos, PieceType.KNIGHT));
        promotions.add(new ChessMove(startPos, endPos, PieceType.ROOK));
        return promotions;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }
}

