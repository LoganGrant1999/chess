import chess.*;
import dataaccess.DataAccessException;

import server.Server;

public class Main {

    // method runs the server on localhost:8080

    public static void main(String[] args) throws DataAccessException {

        //instantiates server and runs it
        Server server = new Server();

        server.run(8080);

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        System.out.println("♕ 240 Chess Server: " + piece);
    }
}