package server;

import com.google.gson.Gson;
import exceptions.NetworkException;
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







}
