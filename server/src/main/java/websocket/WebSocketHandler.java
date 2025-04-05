package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {

    private final AuthDAO auth;

    private final GameDAO game;

    private final ConnectionManager connections = new ConnectionManager();

    private final Map<Integer, ChessGame> games = new ConcurrentHashMap<>();

    public WebSocketHandler(AuthDAO auth, GameDAO game) {
        this.auth = auth;
        this.game = game;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws SQLException, DataAccessException, IOException {

        UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class);

        try{

            switch (cmd.getCommandType()) {

                case CONNECT -> connect(cmd, session);
                case MAKE_MOVE -> makeMove(cmd, session);
                case LEAVE -> leave();
                case RESIGN -> resign();
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

        AuthData authData = auth.getAuth(cmd.getAuthToken());

        GameData gameData = game.getGame(cmd.getGameID());

        if (!games.containsKey(cmd.getGameID())) {

            games.put(cmd.getGameID(), new ChessGame());

        }

        connections.add(authData.username(), cmd.getPlayerColor(), cmd.getGameID(), gameData.gameName(), session);

        ChessGame currGame = games.get(cmd.getGameID());

        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);

        String jsonMsg = new Gson().toJson(msg);

        session.getRemote().sendString(jsonMsg);

        String playerRole = (cmd.getPlayerColor() == null) ? "observer" : cmd.getPlayerColor();

        var message = String.format("%s joined game: %s as %s", authData.username(), gameData.gameName(), playerRole);

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(authData.username(), gameData.gameID(), displayMsg);

    }


    public void makeMove(UserGameCommand cmd, Session session) throws SQLException, DataAccessException, InvalidMoveException, IOException {

        validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        AuthData authData = auth.getAuth(cmd.getAuthToken());

        if (cmd.getPlayerColor() == null || cmd.getMove() == null) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        ChessGame currGame = games.get(cmd.getGameID());

        ChessBoard board = currGame.getBoard();

        ChessGame.TeamColor currColor = currGame.getTeamTurn();

        if (!(ChessGame.TeamColor.valueOf(cmd.getPlayerColor().toUpperCase()) == currColor)){

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        currGame.makeMove(cmd.getMove());

        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);

        connections.broadcast(null, cmd.getGameID(), msg);

        var message = String.format("%s moved %s from %s to %s!", authData.username(),
                board.getPiece(cmd.getMove().getEndPosition()), cmd.getMove().getStartPosition(),
                cmd.getMove().getEndPosition());

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(authData.username(), cmd.getGameID(), displayMsg);

    }

    public void leave() {

    }

    public void resign() {

    }


    public void validateAuthAndGame(String authToken, int gameID) throws SQLException, DataAccessException {

        AuthData authData = auth.getAuth(authToken);

        GameData gameData = game.getGame(gameID);

        if (authData == null || gameData == null) {

            throw new InvalidCredentialsException("Error: unauthorized");

        }
    }

}