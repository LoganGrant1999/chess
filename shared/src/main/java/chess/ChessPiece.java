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

        if (piece == PieceType.KNIGHT){
            return knightMoves(board, myPosition);
        }

        if (piece == PieceType.ROOK){
            return rookMoves(board, myPosition);
        }

        if (piece == PieceType.BISHOP){
            return bishopMoves(board, myPosition);
        }

        if (piece == PieceType.QUEEN){
            return queenMoves(board, myPosition);
        }

        return new ArrayList<ChessMove>();
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(rookMoves(board, startPosition));
        moves.addAll(bishopMoves(board, startPosition));

        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition startPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        // Going Up and Right
        for (int i = 1; i < 9; i ++) {
            ChessPosition upAndRight = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i);

            if (!inbounds(upAndRight) || (board.getPiece(upAndRight) != null && getTeamColor() == board.getPiece(upAndRight).getTeamColor())){
                break;
            }

            moves.add(new ChessMove(startPosition, upAndRight, null));

            if (board.getPiece(upAndRight) != null && getTeamColor() != board.getPiece(upAndRight).getTeamColor()){
                break;
            }
        }

        // Going Up and Left
        for (int i = 1; i < 9; i ++) {
            ChessPosition upAndLeft = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() - i);

            if (!inbounds(upAndLeft) || (board.getPiece(upAndLeft) != null && getTeamColor() == board.getPiece(upAndLeft).getTeamColor())){
                break;
            }

            moves.add(new ChessMove(startPosition, upAndLeft, null));

            if (board.getPiece(upAndLeft) != null && getTeamColor() != board.getPiece(upAndLeft).getTeamColor()){
                break;
            }
        }

        //Going Down and right
        for (int i = 1; i < 9; i ++) {
            ChessPosition downAndRight = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() + i);

            if (!inbounds(downAndRight) || (board.getPiece(downAndRight) != null && getTeamColor() == board.getPiece(downAndRight).getTeamColor())){
                break;
            }

            moves.add(new ChessMove(startPosition, downAndRight, null));

            if (board.getPiece(downAndRight) != null && getTeamColor() != board.getPiece(downAndRight).getTeamColor()){
                break;
            }
        }

        // Going Down and Left
        for (int i = 1; i < 9; i ++) {
            ChessPosition downAndLeft = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() - i);

            if (!inbounds(downAndLeft) || (board.getPiece(downAndLeft) != null && getTeamColor() == board.getPiece(downAndLeft).getTeamColor())){
                break;
            }

            moves.add(new ChessMove(startPosition,(downAndLeft), null));

            if (board.getPiece(downAndLeft) != null && getTeamColor() != board.getPiece(downAndLeft).getTeamColor()){
                break;
            }
        }

        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition startPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        // Going down
        for (int i = 1; i < 9; i++){

            ChessPosition oneDown = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn());

            if (!inbounds(oneDown) || (board.getPiece(oneDown) != null && getTeamColor() == board.getPiece(oneDown).getTeamColor())){
                break;
            }

            moves.add(new ChessMove(startPosition, oneDown, null));

            if (board.getPiece(oneDown) != null && getTeamColor() != board.getPiece(oneDown).getTeamColor()){
                break;
            }
        }

        // Going up
        for (int i = 1; i < 9; i ++) {

            ChessPosition oneUp = new ChessPosition(startPosition.getRow() - i, startPosition.getColumn());

            if (!inbounds(oneUp) || (board.getPiece(oneUp) != null && getTeamColor() == board.getPiece(oneUp).getTeamColor())){
                break;
            }

            moves.add(new ChessMove(startPosition, oneUp, null));

            if (board.getPiece(oneUp) != null && getTeamColor() != board.getPiece(oneUp).getTeamColor()){
                break;
            }
        }

        // Going right
        for (int i = 1; i < 9; i ++) {
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);


            if (!inbounds(oneRight) || board.getPiece(oneRight) != null && getTeamColor() == board.getPiece(oneRight).getTeamColor()){
                break;
            }

            moves.add(new ChessMove(startPosition, oneRight, null));

            if (board.getPiece(oneRight) != null && getTeamColor() != board.getPiece(oneRight).getTeamColor()){
                break;
            }
        }

        //Going Left
        for (int i = 1; i < 9; i ++) {
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i);
            if (!inbounds(oneLeft) || board.getPiece(oneLeft) != null && getTeamColor() == board.getPiece(oneLeft).getTeamColor()){
                break;
            }

            moves.add(new ChessMove(startPosition, oneLeft, null));

            if (board.getPiece(oneLeft) != null && getTeamColor() != board.getPiece(oneLeft).getTeamColor()){
                break;
            }
        }

        return moves;

    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition startPosition) {

        ChessPiece current = board.getPiece(startPosition);

        ArrayList<ChessMove> moves = new ArrayList<>();

        // sets moves for WHITE KNIGHTS
        if (current.getTeamColor() == WHITE) {
            ChessPosition forwardLongRight = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
            ChessPosition forwardLongLeft = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1);
            ChessPosition forwardShortRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2);
            ChessPosition forwardShortLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2);
            ChessPosition backwardLongRight = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1);
            ChessPosition backwardLongLeft = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1);
            ChessPosition backwardShortRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2);
            ChessPosition backwardShortLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 2);

            if (inbounds(forwardLongRight) && (board.getPiece(forwardLongRight) == null || getTeamColor() != board.getPiece(forwardLongRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardLongRight, null));
            }

            if (inbounds(forwardLongLeft) && (board.getPiece(forwardLongLeft) == null || getTeamColor() != board.getPiece(forwardLongLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardLongLeft, null));
            }

            if (inbounds(forwardShortRight) && (board.getPiece(forwardShortRight) == null || getTeamColor() != board.getPiece(forwardShortRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardShortRight, null));
            }

            if (inbounds(forwardShortLeft) && (board.getPiece(forwardShortLeft) == null || getTeamColor() != board.getPiece(forwardShortLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardShortLeft, null));
            }

            if (inbounds(backwardLongRight) && (board.getPiece(backwardLongRight) == null || getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardLongRight, null));
            }

            if (inbounds(backwardLongLeft) && (board.getPiece(backwardLongLeft) == null || getTeamColor() != board.getPiece(backwardLongLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardLongLeft, null));
            }

            if (inbounds(backwardShortRight) && (board.getPiece(backwardShortRight) == null || getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardShortRight, null));
            }

            if (inbounds(backwardShortLeft) && (board.getPiece(backwardShortLeft) == null || getTeamColor() != board.getPiece(backwardShortLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardShortLeft, null));
            }
        }

        // sets moves for BLACK KNIGHTS
        if (current.getTeamColor() == BLACK) {
            ChessPosition forwardLongRight = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1);
            ChessPosition forwardLongLeft = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1);
            ChessPosition forwardShortRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2);
            ChessPosition forwardShortLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 2);
            ChessPosition backwardLongRight = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
            ChessPosition backwardLongLeft = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1);
            ChessPosition backwardShortRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2);
            ChessPosition backwardShortLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2);

            if (inbounds(forwardLongRight) && (board.getPiece(forwardLongRight) == null || getTeamColor() != board.getPiece(forwardLongRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardLongRight, null));
            }

            if (inbounds(forwardLongLeft) && (board.getPiece(forwardLongLeft) == null || getTeamColor() != board.getPiece(forwardLongLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardLongLeft, null));
            }

            if (inbounds(forwardShortRight) && (board.getPiece(forwardShortRight) == null || getTeamColor() != board.getPiece(forwardShortRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardShortRight, null));
            }

            if (inbounds(forwardShortLeft) && (board.getPiece(forwardShortLeft) == null || getTeamColor() != board.getPiece(forwardShortLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, forwardShortLeft, null));
            }

            if (inbounds(backwardLongRight) && (board.getPiece(backwardLongRight) == null || getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardLongRight, null));
            }

            if (inbounds(backwardLongLeft) && (board.getPiece(backwardLongLeft) == null || getTeamColor() != board.getPiece(backwardLongLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardLongLeft, null));
            }

            if (inbounds(backwardShortRight) && (board.getPiece(backwardShortRight) == null || getTeamColor() != board.getPiece(backwardLongRight).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardShortRight, null));
            }

            if (inbounds(backwardShortLeft) && (board.getPiece(backwardShortLeft) == null || getTeamColor() != board.getPiece(backwardShortLeft).getTeamColor())) {
                moves.add(new ChessMove(startPosition, backwardShortLeft, null));
            }
        }

        return moves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition startPosition) {
        ChessPiece current = board.getPiece(startPosition);

        ArrayList<ChessMove> moves = new ArrayList<>();

        // sets moves for the WHITE KING
        if (current.getTeamColor() == WHITE){
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition oneBehind = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
            ChessPosition backwardDiagonalRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            ChessPosition backwardDiagonalLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);

            if (inbounds(oneAhead) && (board.getPiece(oneAhead) == null || getTeamColor() != board.getPiece(oneAhead).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (inbounds(oneBehind) && (board.getPiece(oneBehind) == null || getTeamColor() != board.getPiece(oneBehind).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneBehind, null));
            }

            if (inbounds(oneRight) && (board.getPiece(oneRight) == null || getTeamColor() != board.getPiece(oneRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneRight, null));
            }

            if (inbounds(oneLeft) && (board.getPiece(oneLeft) == null || getTeamColor() != board.getPiece(oneLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneLeft, null));
            }

            if (inbounds(forwardDiagonalRight) && (board.getPiece(forwardDiagonalRight) == null || getTeamColor() != board.getPiece(forwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, forwardDiagonalRight, null));
            }

            if (inbounds(forwardDiagonalLeft) && (board.getPiece(forwardDiagonalLeft) == null || getTeamColor() != board.getPiece(forwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, forwardDiagonalLeft, null));
            }

            if (inbounds(backwardDiagonalRight) && (board.getPiece(backwardDiagonalRight) == null || getTeamColor() != board.getPiece(backwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, backwardDiagonalRight, null));
            }

            if (inbounds(backwardDiagonalLeft) && (board.getPiece(backwardDiagonalLeft) == null || getTeamColor() != board.getPiece(backwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, backwardDiagonalLeft, null));
            }
        }

        // sets moves for the BLACK KING
        if (current.getTeamColor() == BLACK){
            ChessPosition oneAhead = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            ChessPosition oneBehind = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            ChessPosition oneLeft = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
            ChessPosition oneRight = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            ChessPosition forwardDiagonalLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            ChessPosition backwardDiagonalRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition backwardDiagonalLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);

            if (inbounds(oneAhead) && (board.getPiece(oneAhead) == null || getTeamColor() != board.getPiece(oneAhead).getTeamColor())) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (inbounds(oneBehind) && (board.getPiece(oneBehind) == null || getTeamColor() != board.getPiece(oneBehind).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneBehind, null));
            }

            if (inbounds(oneRight) && (board.getPiece(oneRight) == null || getTeamColor() != board.getPiece(oneRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneRight, null));
            }

            if (inbounds(oneLeft) && (board.getPiece(oneLeft) == null || getTeamColor() != board.getPiece(oneLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, oneLeft, null));
            }

            if (inbounds(forwardDiagonalRight) && (board.getPiece(forwardDiagonalRight) == null || getTeamColor() != board.getPiece(forwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, forwardDiagonalRight, null));
            }

            if (inbounds(forwardDiagonalLeft) && (board.getPiece(forwardDiagonalLeft) == null || getTeamColor() != board.getPiece(forwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, forwardDiagonalLeft, null));
            }

            if (inbounds(backwardDiagonalRight) && (board.getPiece(backwardDiagonalRight) == null || getTeamColor() != board.getPiece(backwardDiagonalRight).getTeamColor())){
                moves.add(new ChessMove (startPosition, backwardDiagonalRight, null));
            }

            if (inbounds(backwardDiagonalLeft) && (board.getPiece(backwardDiagonalLeft) == null || getTeamColor() != board.getPiece(backwardDiagonalLeft).getTeamColor())){
                moves.add(new ChessMove (startPosition, backwardDiagonalLeft, null));
            }
        }

        return moves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition startPosition) {

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

            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null && getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));
            }

            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && startPosition.getRow() != 7) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }

            if (startPosition.getRow() == 7 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(startPosition, oneAhead));
            }

            if (startPosition.getRow() == 7 && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalLeft));
            }

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

            if (inbounds(twoAhead) && startPosition.getRow() == 7 && board.getPiece(oneAhead) == null && board.getPiece(oneAhead) == null) {
                moves.add(new ChessMove(startPosition, twoAhead, null));
            }

            if (inbounds(oneAhead) && board.getPiece(oneAhead) == null && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, oneAhead, null));
            }

            if (inbounds(diagonalRight) && board.getPiece(diagonalRight) != null && getTeamColor() != board.getPiece(diagonalRight).getTeamColor() && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, diagonalRight, null));
            }

            if (inbounds(diagonalLeft) && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor() && startPosition.getRow() != 2) {
                moves.add(new ChessMove(startPosition, diagonalLeft, null));
            }

            if (startPosition.getRow() == 2 && board.getPiece(oneAhead) == null) {
                moves.addAll(promoter(startPosition, oneAhead));
            }

            if (startPosition.getRow() == 2 && board.getPiece(diagonalLeft) != null && getTeamColor() != board.getPiece(diagonalLeft).getTeamColor()) {
                moves.addAll(promoter(startPosition, diagonalLeft));
            }

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

