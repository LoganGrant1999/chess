package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {

    public String userName;

    public Session session;

    public int gameID;

    public String gameName;

    public String playerRole;

    public Connection(String userName, String playerRole, int gameID, String gameName, Session session){

        this.userName = userName;

        this.playerRole = playerRole;

        this.gameID = gameID;

        this.gameName = gameName;

        this.session = session;

    }

    public void send(String msg) throws IOException{

        session.getRemote().sendString(msg);
    }
}
