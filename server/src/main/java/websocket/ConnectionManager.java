package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

   public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

   public void add(String userName, String playerRole, int gameID, String gameName, Session session){

      var connection = new Connection(userName, playerRole, gameID, gameName, session);

      connections.put(userName, connection);

   }

   public void remove(String userName){

      connections.remove(userName);

   }

   public void broadcast (String excludeUserName, ServerMessage msg) throws IOException {

      var removeList = new ArrayList<Connection>();

      for (var c : connections.values()) {

         if (c.session.isOpen()) {

            if (!c.userName.equals(excludeUserName)) {

               c.send(msg.toString());
            }
         } else {

            removeList.add(c);
         }
      }

      for (var c : removeList) {

         connections.remove(c.userName);
      }
   }
}
