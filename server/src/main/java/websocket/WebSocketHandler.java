package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exceptions.InvalidCredentialsException;
import exceptions.NetworkException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final AuthDAO authDAO;

    private final GameDAO gameDAO;

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(AuthDAO auth, GameDAO game) {
        this.authDAO = auth;
        this.gameDAO = game;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws SQLException, DataAccessException, IOException {

        UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class);

        try {

            switch (cmd.getCommandType()) {

                case CONNECT -> connect(cmd, session);
                case MAKE_MOVE -> makeMove(cmd, session);
                case LEAVE -> leave(cmd);
                case RESIGN -> resign(cmd);
            }

        } catch (Exception e) {

            var msg = e.getMessage();

            ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, msg);

            String jsonMsg = new Gson().toJson(displayMsg);

            session.getRemote().sendString(jsonMsg);

        }
    }

    public void connect(UserGameCommand cmd, Session session) throws SQLException, DataAccessException, IOException {

        validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        AuthData authData = authDAO.getAuth(cmd.getAuthToken());

        GameData gameData = gameDAO.getGame(cmd.getGameID());

        connections.add(authData.username(), cmd.getPlayerColor(), cmd.getGameID(), gameData.gameName(), session);

        ChessGame currGame = gameData.game();

        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);

        String jsonMsg = new Gson().toJson(msg);

        session.getRemote().sendString(jsonMsg);

        String playerRole = (cmd.getPlayerColor() == null) ? "observer" : cmd.getPlayerColor();

        var message = String.format("%s joined game: %s as %s", authData.username(), gameData.gameName(), playerRole);

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(authData.username(), gameData.gameID(), displayMsg);
    }

    public void makeMove(UserGameCommand cmd, Session session)
            throws SQLException, DataAccessException, InvalidMoveException, IOException {

        AuthData authData = validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        int gameID = cmd.getGameID();

        GameData gameData = gameDAO.getGame(gameID);

        String userName = authData.username();

        ChessGame.TeamColor currColor = gameData.game().getTeamTurn();


        if (gameData.game().gameOver()) {

            String message = String.format("%s attempted a move, but the game is already over.", authData.username());

            ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, message);

            String jsonMsg = new Gson().toJson(displayMsg);

            session.getRemote().sendString(jsonMsg);

            return;
        }

        if (cmd.getMove() == null) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        ChessBoard board = gameData.game().getBoard();

        if (currColor == ChessGame.TeamColor.WHITE && !userName.equals(gameData.whiteUsername())) {
            throw new InvalidCredentialsException("Error: unauthorized");
        }

        if (currColor == ChessGame.TeamColor.BLACK && !userName.equals(gameData.blackUsername())) {
            throw new InvalidCredentialsException("Error: unauthorized");
        }

        ChessMove move = cmd.getMove();

        ChessPosition start = move.getStartPosition();

        ChessPosition end = move.getEndPosition();

        gameData.game().makeMove(cmd.getMove());

        gameDAO.updateGame(cmd.getGameID(), gameData);

        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());

        connections.broadcast(null, cmd.getGameID(), msg);

        var message = String.format("%s moved %s from %s to %s!", authData.username(),
                getPieceName(board.getPiece(cmd.getMove().getEndPosition()))
                , convertPos(start),
                convertPos(end));

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(authData.username(), cmd.getGameID(), displayMsg);

        ChessGame.TeamColor opponent = getOpponent(currColor);

        if (gameData.game().isInCheckmate(opponent)) {

            gameData.game().setGameIsOver();

            gameDAO.updateGame(cmd.getGameID(), gameData);

            var checkMsg = String.format("%s is in checkmate!", opponent);

            ServerMessage displayCheckMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMsg);

            connections.broadcast(null, cmd.getGameID(), displayCheckMsg);


        } else if (gameData.game().isInCheck(opponent)) {

            var checkMsg = String.format("%s is in check!", opponent);

            ServerMessage displayCheckMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMsg);

            connections.broadcast(null, cmd.getGameID(), displayCheckMsg);

        } else if (gameData.game().isInStalemate(opponent)) {

            gameData.game().setGameIsOver();

            gameDAO.updateGame(cmd.getGameID(), gameData);

            var checkMsg = String.format("%s is in stalemate!", opponent);

            ServerMessage displayCheckMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMsg);

            connections.broadcast(null, cmd.getGameID(), displayCheckMsg);

        }
    }

    public void leave(UserGameCommand cmd) throws SQLException, DataAccessException, IOException {

        AuthData authData = validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        Connection conn = connections.getConnection(authData.username());

        GameData currGame = gameDAO.getGame(cmd.getGameID());

        if (!authData.username().equals(currGame.whiteUsername()) && !authData.username().equals(currGame.blackUsername())) {

            var message = String.format("Observer %s has left the game", authData.username());

            ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

            connections.broadcast(authData.username(), cmd.getGameID(), displayMsg);

            connections.remove(authData.username());

        } else {

            var message = String.format("Player %s has left the game", authData.username());

            ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

            connections.broadcast(authData.username(), cmd.getGameID(), displayMsg);

            if (authData.username().equals(currGame.whiteUsername())) {

                GameData updatedGame = new GameData(currGame.gameID(),null,
                        currGame.blackUsername(), currGame.gameName(), currGame.game());

                gameDAO.updateGame(updatedGame.gameID(), updatedGame);

            } else if(authData.username().equals(currGame.blackUsername())){

                GameData updatedGame = new GameData(currGame.gameID(), currGame.whiteUsername(),
                        null, currGame.gameName(), currGame.game());

                gameDAO.updateGame(updatedGame.gameID(), updatedGame);

            }

            connections.remove(authData.username());

        }
    }

    public void resign(UserGameCommand cmd) throws SQLException, DataAccessException, IOException, NetworkException, InvalidMoveException {

        AuthData authData = validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        GameData gameData = gameDAO.getGame(cmd.getGameID());
        ChessGame currGame = gameData.game();


        if (currGame.gameOver()) {

            throw new InvalidMoveException("Error: Game already over");
        }

        if (!Objects.equals(authData.username(), gameData.whiteUsername()) &&
                !Objects.equals(authData.username(), gameData.blackUsername())) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        currGame.setGameIsOver();

        gameDAO.updateGame(cmd.getGameID(), gameData);

        ChessGame.TeamColor opponent;

        if (Objects.equals(authData.username(), gameData.whiteUsername())) {

            opponent = ChessGame.TeamColor.BLACK;

        } else if (Objects.equals(authData.username(), gameData.blackUsername())) {

            opponent = ChessGame.TeamColor.WHITE;

        } else {

            throw new NetworkException(500, "Error: Observer can't resign");
        }

        var message = String.format("%s has resigned from the game. %s wins!", authData.username(), opponent);

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(null, cmd.getGameID(), displayMsg);

    }


    public AuthData validateAuthAndGame(String authToken, int gameID) throws SQLException, DataAccessException {

        AuthData authData = authDAO.getAuth(authToken);

        GameData gameData = gameDAO.getGame(gameID);

        if (authData == null || gameData == null) {

            throw new InvalidCredentialsException("Error: unauthorized");

        }

        return authData;
    }

    public ChessGame.TeamColor getOpponent(ChessGame.TeamColor color) {

        if (color == ChessGame.TeamColor.WHITE) {

            return ChessGame.TeamColor.BLACK;

        } else {

            return ChessGame.TeamColor.WHITE;
        }
    }

    private String getPieceName(ChessPiece piece) {
        if (piece == null) return "Empty Space";

        return switch (piece.getPieceType()) {
            case PAWN -> "Pawn";
            case ROOK -> "Rook";
            case KNIGHT -> "Knight";
            case BISHOP -> "Bishop";
            case QUEEN -> "Queen";
            case KING -> "King";
        };
    }

    private String convertPos(ChessPosition pos){

        String finalCol = null;

        int col = pos.getColumn();

        switch(col){

            case 1 -> finalCol = "A";
            case 2 -> finalCol = "B";
            case 3 -> finalCol = "C";
            case 4 -> finalCol = "D";
            case 5 -> finalCol = "E";
            case 6 -> finalCol = "F";
            case 7 -> finalCol = "G";
            case 8 -> finalCol = "H";
        }

        return finalCol + pos.getRow();
    }
}