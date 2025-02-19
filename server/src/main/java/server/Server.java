package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import handlers.LoginHandler;
import handlers.LogoutHandler;
import handlers.RegisterHandler;
import spark.*;

public class Server {

    private MemoryUserDAO userDAO = new MemoryUserDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", new RegisterHandler(userDAO, authDAO));
        Spark.post("/session", new LoginHandler(userDAO, authDAO));
        Spark.post("/session", new LogoutHandler(authDAO));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
