package dataaccess;

import chess.ChessGame;
import exceptions.MissingDataException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import server.Server;
import service.ClearService;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    private static MySqlAuthDAO auth;
    private static MySqlGameDAO game;
    private static MySqlUserDAO user;
    private static Server server;
    private static ClearService clearer;
    private static UserData userData;
    private static AuthData authData;
    private static String gameName;

    @BeforeEach
    public void setup() throws Exception {

        auth = new MySqlAuthDAO();

        game = new MySqlGameDAO();

        user = new MySqlUserDAO();

        userData = new UserData("test", "password", "email@email");

        authData = new AuthData("testAuthToken", "password");

        gameName = "game";

        server = new Server();

        server.run(0);

        DatabaseManager.configureDatabase();

        clearer = new ClearService();

        clearer.clearAllData(auth, game, user);

        DatabaseManager.configureDatabase();

        auth.createAuth(authData);

        user.createUser(userData);

        game.createGame(gameName);
    }

    @AfterEach
    public void stopServer(){

        server.stop();
    }

    @Test
    @Order(1)
    @DisplayName("Successful Create User Test")
    void successfulCreateUser() throws DataAccessException {

        UserData testUserData = new UserData("username", "password", "email@email.com");

        user.createUser(testUserData);

        server.stop();

        server.run(0);

        UserData storedUserData = user.getUser(testUserData.username());

        assertEquals(testUserData.username(), storedUserData.username(), "Username not stored correctly");

        assertEquals(testUserData.email(), storedUserData.email(), "Email not stored correctly");

        assertTrue(BCrypt.checkpw(testUserData.password(),
                storedUserData.password()), "Password not stored correctly");
    }

    @Test
    @Order(2)
    @DisplayName("Unsuccessful Create User Test")
    void unsuccessfulCreateUser() {

        UserData userData = new UserData(null, "password", "email");

        assertThrows(DataAccessException.class, () -> user.createUser(userData), "Not Thrown");

    }

    @Test
    @Order(3)
    @DisplayName("Successful Get User Test")
    void successfulGetUser() throws DataAccessException {

        server.stop();

        server.run(0);

        UserData storedUserData = user.getUser(userData.username());

        assertEquals(storedUserData.username(), userData.username(), "Username not stored correctly");

        assertEquals(storedUserData.email(), userData.email(), "Email not stored correctly");

        assertTrue(BCrypt.checkpw(userData.password(), storedUserData.password()),
                "password not stored correctly");
    }

    @Test
    @Order(4)
    @DisplayName("Unsuccessful Get User Test")
    void unsuccessfulGetUser() {

        assertThrows(MissingDataException.class, () -> user.getUser(null), "Not Thrown");
    }

    @Test
    @Order(5)
    @DisplayName("Successful User Clear")
    void clearUserTable() throws DataAccessException {

        UserData testUser = new UserData("testUser", "password", "email@email.com");

        user.createUser(testUser);

        assertNotNull(user.getUser(testUser.username()), "Create User Didn't Work");

        assertNotNull(user.getUser(userData.username()), "Create User Didn't Work");

        user.clear();

        assertNull(user.getUser(testUser.username()), "Table not truncated");

        assertNull(user.getUser(authData.username()), "Table not truncated");

    }

    @Test
    @Order(6)
    @DisplayName("Successful createAuth")
    void successfulCreateAuth() throws DataAccessException {

        AuthData testAuthData = new AuthData("fakeAuthToken", "testUser");

        auth.createAuth(testAuthData);

        server.stop();

        server.run(0);

        AuthData storedAuthData = auth.getAuth(testAuthData.authToken());

        assertEquals(storedAuthData.authToken(), testAuthData.authToken(), "authToken stored incorrectly");

        assertEquals(storedAuthData.username(), testAuthData.username(), "username stored incorrectly");
    }

    @Test
    @Order(7)
    @DisplayName("Unsuccessful createAuth")
    void unsuccessfulCreateAuth() {

        AuthData testAuthData = new AuthData(null, null);

        assertThrows(DataAccessException.class, () -> auth.createAuth(testAuthData), "Not Thrown");
    }

    @Test
    @Order(8)
    @DisplayName("Successful getAuth")
    void successfulGetAuth() throws DataAccessException {

        server.stop();

        server.run(0);

        AuthData storedAuthData = auth.getAuth(authData.authToken());

        assertEquals(authData.authToken(), storedAuthData.authToken(), "AuthToken not retrieved correctly");

        assertEquals(authData.username(), storedAuthData.username(), "Username not retrieved correctly");

        assertNotNull(storedAuthData.username(), "Username not stored persistently");

        assertNotNull(storedAuthData.authToken(), "AuthToken not stored persistently");
    }

    @Test
    @Order(9)
    @DisplayName("Unsuccessful getAuth")
    void unsuccessfulGetAuth(){

        assertThrows(MissingDataException.class, () -> auth.getAuth(null), "Not Thrown");
    }

    @Test
    @Order(10)
    @DisplayName("Successful Remove Auth")
    void successfulRemove() throws DataAccessException {

        assertEquals(authData, auth.getAuth(authData.authToken()));

        auth.remove(authData.authToken());

        assertNull(auth.getAuth(authData.authToken()));
    }

    @Test
    @Order(11)
    @DisplayName("Unsuccessful Remove Auth")
    void unsuccessfulRemove() {

        assertThrows(DataAccessException.class, () -> auth.remove(null));
    }


    @Test
    @Order(12)
    @DisplayName("Successful Clear Auth Table")
    void clearAuthTable() throws DataAccessException {

        AuthData testAuth = new AuthData("fakeAuthToken", "FakeUsername");

        auth.createAuth(testAuth);

        assertNotNull(auth.getAuth(testAuth.authToken()), "AuthToken not stored correctly");

        assertNotNull(auth.getAuth(authData.authToken()), "AuthToken not stored correctly");

        auth.clear();

        assertNull(auth.getAuth(testAuth.authToken()));

        assertNull(auth.getAuth(authData.authToken()));

    }

    @Test
    @Order(13)
    @DisplayName("Successful Create Game")
    void successfulCreateGame() throws DataAccessException {

        String testGameName = "testGameName";

        game.createGame(testGameName);

        server.stop();

        server.run(0);

        GameData storedGameData = game.getGame(2);

        assertEquals(gameName, game.getGame(1).gameName(), "Game not stored persistently");

        assertEquals(testGameName, storedGameData.gameName(), "Game not stored persistently");
    }

    @Test
    @Order(14)
    @DisplayName("Unsuccessful Create Game")
    void unsuccessfulCreateGame() {

        assertThrows(DataAccessException.class, () -> game.createGame(null));
    }


    @Test
    @Order(15)
    @DisplayName("Successful getGame")
    void successfulGetGame() throws DataAccessException {

        server.stop();

        server.run(0);

        GameData storedGameData = game.getGame(1);

        assertEquals(gameName, storedGameData.gameName());
    }

    @Test
    @Order(16)
    @DisplayName("Unsuccessful getGame")
    void unsuccessfulGetGame() {

        assertThrows(MissingDataException.class, () -> game.getGame(0));
    }

    @Test
    @Order(17)
    @DisplayName("Successful listGames")
    void listGames() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void clearGameTable() {
    }
}
