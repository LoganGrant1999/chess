package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.io.ssl.ALPNProcessor;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

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
    public void onMessage(Session session, String message) throws SQLException, DataAccessException {

        UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class);

        switch (cmd.getCommandType()) {

            case CONNECT -> connect(cmd, session);
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }


    public void connect(UserGameCommand cmd, Session sesh) throws SQLException, DataAccessException {

        if (auth.getAuth(cmd.getAuthToken()) == null) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        AuthData authData = auth.getAuth(cmd.getAuthToken());

        if (game.getGame(cmd.getGameID()) == null){

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        GameData gameData = game.getGame(cmd.getGameID());

        if (!games.containsKey(cmd.getGameID())) {

            games.put(cmd.getGameID(), new ChessGame());

        }

        connections.add(authData.username(), cmd.getPlayerColor(), cmd.getGameID(), gameData.gameName(), sesh);

        ChessGame currGame = games.get(cmd.getGameID());

        ServerMessage msg = new ServerMessage(currGame, LOAD_GAME);

    }

    public void makeMove() {

    }

    public void leave() {

    }

    public void resign() {

    }

}
