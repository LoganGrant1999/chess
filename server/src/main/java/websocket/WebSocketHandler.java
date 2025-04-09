package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;
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
import java.util.Map;
import java.util.Objects;
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
                case MAKE_MOVE -> makeMove(cmd);
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


    public void makeMove(UserGameCommand cmd)
            throws SQLException, DataAccessException, InvalidMoveException, IOException {

        AuthData authData = validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        ChessGame currGame = games.get(cmd.getGameID());

        if (currGame.gameOver()) {
            String message = String.format("%s attempted a move, but the game is already over.", authData.username());

            ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

            connections.broadcast(null, cmd.getGameID(), displayMsg);

            return;
        }


        if (cmd.getPlayerColor() == null || cmd.getMove() == null) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        ChessBoard board = currGame.getBoard();

        ChessGame.TeamColor currColor = currGame.getTeamTurn();

        if (!(ChessGame.TeamColor.valueOf(cmd.getPlayerColor().toUpperCase()) == currColor)) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        currGame.makeMove(cmd.getMove());

        game.updateGame(cmd.getGameID(), currGame);

        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);

        connections.broadcast(null, cmd.getGameID(), msg);

        var message = String.format("%s moved %s from %s to %s!", authData.username(),
                board.getPiece(cmd.getMove().getEndPosition()), cmd.getMove().getStartPosition(),
                cmd.getMove().getEndPosition());

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(authData.username(), cmd.getGameID(), displayMsg);

        ChessGame.TeamColor opponent = getOpponent(currColor);

        if (currGame.isInCheckmate(opponent)) {

            currGame.setGameIsOver();

            game.updateGame(cmd.getGameID(), currGame);

            var checkMsg = String.format("%s is in checkmate!", opponent);

            ServerMessage displayCheckMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMsg);

            connections.broadcast(null, cmd.getGameID(), displayCheckMsg);


        } else if (currGame.isInCheck(opponent)) {

            var checkMsg = String.format("%s is in check!", opponent);

            ServerMessage displayCheckMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMsg);

            connections.broadcast(null, cmd.getGameID(), displayCheckMsg);

        } else if (currGame.isInStalemate(opponent)) {

            currGame.setGameIsOver();

            game.updateGame(cmd.getGameID(), currGame);

            var checkMsg = String.format("%s is in stalemate!", opponent);

            ServerMessage displayCheckMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMsg);

            connections.broadcast(null, cmd.getGameID(), displayCheckMsg);

        }
    }

    public void leave(UserGameCommand cmd) throws SQLException, DataAccessException, IOException {

        AuthData authData = validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        Connection conn = connections.getConnection(authData.username());

        GameData currGame = game.getGame(cmd.getGameID());

        if (conn.playerRole == null) {

            var message = String.format("Observer %s has left the game", authData.username());

            ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

            connections.broadcast(authData.username(), cmd.getGameID(), displayMsg);

            connections.remove(authData.username());
        }

        else if (conn.playerRole.equalsIgnoreCase("WHITE")
                || conn.playerRole.equalsIgnoreCase("BLACK")) {

            var message = String.format("Player %s has left the game", authData.username());

            ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

            connections.broadcast(authData.username(), cmd.getGameID(), displayMsg);

            GameData updatedGame = game.joinGame(currGame.gameID(),null, conn.playerRole, currGame.gameName());

            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame.game());

            connections.broadcast(authData.username(), cmd.getGameID(), msg);

            connections.remove(authData.username());

        } else {
            throw new DataAccessException("Neither observer nor player");
        }
    }

    public void resign(UserGameCommand cmd) throws SQLException, DataAccessException, IOException, NetworkException, InvalidMoveException {

        AuthData authData = validateAuthAndGame(cmd.getAuthToken(), cmd.getGameID());

        GameData gameData = game.getGame(cmd.getGameID());

        if (!Objects.equals(authData.username(), gameData.whiteUsername()) &&
                !Objects.equals(authData.username(), gameData.blackUsername())) {

            throw new InvalidCredentialsException("Error: unauthorized");
        }


        ChessGame currGame = gameData.game();

        if (currGame.gameOver()) {

            throw new InvalidMoveException("Error: Game already over");

        }

        currGame.resign();

        currGame.setGameIsOver();

        game.updateGame(cmd.getGameID(), gameData.game());

        ChessGame.TeamColor opponent;

        if (Objects.equals(authData.username(), gameData.whiteUsername())) {

            opponent = ChessGame.TeamColor.BLACK;

        } else if (Objects.equals(authData.username(), gameData.blackUsername())){

            opponent = ChessGame.TeamColor.WHITE;

        } else {

            throw new NetworkException(500, "Error: Observer can't resign");
        }

        var message = String.format("%s has resigned from the game. %s wins!", authData.username(), opponent);

        ServerMessage displayMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connections.broadcast(null, cmd.getGameID(), displayMsg);

    }


    public AuthData validateAuthAndGame(String authToken, int gameID) throws SQLException, DataAccessException {

        AuthData authData = auth.getAuth(authToken);

        GameData gameData = game.getGame(gameID);

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
}