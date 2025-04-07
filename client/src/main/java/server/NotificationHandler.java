package server;

import exceptions.NetworkException;
import websocket.messages.ServerMessage;

public interface NotificationHandler {

    void notify(ServerMessage message) throws NetworkException;
}
