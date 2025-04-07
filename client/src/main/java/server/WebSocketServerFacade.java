package server;

import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.NetworkException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketServerFacade extends Endpoint {


    Session session;
    NotificationHandler notificationHandler;

    public WebSocketServerFacade(String url, NotificationHandler notificationHandler) throws NetworkException {

        try{

            url = url.replace("http", "ws");

            URI socketURI = new URI(url + "/ws");

            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>(){

                @Override
                public void onMessage(String s) {

                    ServerMessage msg = new Gson().fromJson(s, ServerMessage.class);

                    notificationHandler.notify(msg);

                }
            });

        } catch (URISyntaxException | DeploymentException | IOException e){

            throw new NetworkException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }


    public void connectToGame(String authToken, int gameID, String playerColor) throws NetworkException {

        try {
            UserGameCommand cmd =
                    new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                            null, authToken, gameID, playerColor);

            String json = new Gson().toJson(cmd);

            this.session.getBasicRemote().sendText(json);

        } catch (IOException e) {

            throw new NetworkException(500, e.getMessage());

        }

    }

    public void makeMove(String authToken, int gameID, String playerColor, ChessMove move) throws NetworkException {

        try {

            UserGameCommand cmd =
                    new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, move, authToken, gameID, playerColor);

            String json = new Gson().toJson(cmd);

            this.session.getBasicRemote().sendText(json);

        } catch (IOException e) {

            throw new NetworkException(500, e.getMessage());
        }

    }


    public void leaveGame(String authToken, int gameID) throws NetworkException {

        try {

            UserGameCommand cmd =
                    new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                            null, authToken, gameID, null);

            String json = new Gson().toJson(cmd);

            this.session.getBasicRemote().sendText(json);

            this.session.close();

        } catch (IOException e) {

            throw new NetworkException(500, e.getMessage());
        }

    }


    public void resign(String authToken, int gameID, String playerColor) throws NetworkException {

        try {

            UserGameCommand cmd =
                    new UserGameCommand(UserGameCommand.CommandType.RESIGN, null, authToken, gameID, playerColor);

            String json = new Gson().toJson(cmd);

            this.session.getBasicRemote().sendText(json);

        } catch (IOException e) {

            throw new NetworkException(500, e.getMessage());
        }

    }
}
