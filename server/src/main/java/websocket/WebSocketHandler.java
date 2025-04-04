package websocket;

import chess.ChessGame;
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

    private String playerColor;

    private final AuthDAO auth;

    private final GameDAO game;

    private final ConnectionManager connections = new ConnectionManager();

    private final Map<Integer, ChessGame> games = new ConcurrentHashMap<>();

    public WebSocketHandler(AuthDAO auth, GameDAO game, String playerColor) {
        this.auth = auth;
        this.game = game;
        this.playerColor = playerColor;
    }

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
                case MAKE_MOVE -> makeMove();
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


    public void connect(UserGameCommand cmd, Session sesh) throws SQLException, DataAccessException, IOException {

        if (auth.getAuth(cmd.getAuthToken()) == null) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        AuthData authData = auth.getAuth(cmd.getAuthToken());

        if (game.getGame(cmd.getGameID()) == null) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        GameData gameData = game.getGame(cmd.getGameID());

        if (!games.containsKey(cmd.getGameID())) {

            games.put(cmd.getGameID(), new ChessGame());

        }

        connections.add(authData.username(), cmd.getPlayerColor(), cmd.getGameID(), gameData.gameName(), sesh);

        ChessGame currGame = games.get(cmd.getGameID());

        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);

        String jsonMsg = new Gson().toJson(msg);

        sesh.getRemote().sendString(jsonMsg);

        if (cmd.getPlayerColor() == null) {

            playerColor = "observer";

        } else {

            playerColor = cmd.getPlayerColor();
        }

        var message = String.format("%s joined game: %s as %s", authData.username(), gameData.gameName(), playerColor);

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(authData.username(), gameData.gameID(), displayMsg);

    }


    public void makeMove() {

    }

    public void leave() {

    }

    public void resign() {

    }

}