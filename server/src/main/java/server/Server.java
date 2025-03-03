package server;

import dataaccess.*;
import handlers.*;
import model.AuthData;
import model.GameData;
import spark.*;

public class Server {

    /*initializes AuthDAO, GameDAO, and UserDAO objects to make calling their
    class methods easier
    */
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public Server() {
        this.userDAO = new DBUserDAO();
        this.gameDAO = new DBGameDAO();
        this.authDAO = new DBAuthDAO();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", new RegisterHandler(userDAO, authDAO));
        Spark.post("/session", new LoginHandler(userDAO, authDAO));
        Spark.delete("/session", new LogoutHandler(authDAO));
        Spark.post("/game", new CreateGameHandler(gameDAO, authDAO));
        Spark.get("/game", new ListGamesHandler(authDAO, gameDAO));
        Spark.put("/game", new JoinGameHandler(userDAO, authDAO, gameDAO));
        Spark.delete("/db", new ClearHandler(authDAO, gameDAO, userDAO));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
