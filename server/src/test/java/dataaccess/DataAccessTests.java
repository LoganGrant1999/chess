package dataaccess;

import chess.ChessGame;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import model.AuthData;
import model.GameData;
import model.ListGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import server.Server;
import service.ClearService;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    //Creates private static variables to be instantiated before each test in setup method
    private static MySqlAuthDAO auth;
    private static MySqlGameDAO game;
    private static MySqlUserDAO user;
    private static Server server;
    private static ClearService clearer;
    private static UserData userData;
    private static AuthData authData;
    private static GameData gameData;
    private static GameData newGameData;
    private static String gameName;


    /*Creates objects and variables needed for the unit tests that are all instantiated before each test runs
    Also starts server and clears database before each test
     */
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

        gameData = game.getGame(1);

        newGameData = game.joinGame(1, userData.username(), "BLACK", "testGame1");
    }

    //stops test server after each test
    @AfterEach
    public void stopServer(){

        server.stop();
    }

    //Tests that userdata can be correctly stored in user table and that it persists after server stops/restarts
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


    //throws DataAccessException when username field is null in a call to createUser
    @Test
    @Order(2)
    @DisplayName("Unsuccessful Create User Test")
    void unsuccessfulCreateUser() {

        UserData userData = new UserData(null, "password", "email");

        assertThrows(DataAccessException.class, () -> user.createUser(userData), "Not Thrown");

    }

    //shows that user information persists after server stops/restarts and that it can be retrieved with getUser
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

    //Shows that MissingDataException is thrown when null username is passed into getUser
    @Test
    @Order(4)
    @DisplayName("Unsuccessful Get User Test")
    void unsuccessfulGetUser() {

        assertThrows(MissingDataException.class, () -> user.getUser(null), "Not Thrown");
    }

    //tests that user table can be cleared of all existing data with clear method
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

    //Shows that createAuth successfully stores data that persists when server stops/restarts
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

    //Shows that DataAccessException is thrown when null values are passed into createAuth
    @Test
    @Order(7)
    @DisplayName("Unsuccessful createAuth")
    void unsuccessfulCreateAuth() {

        AuthData testAuthData = new AuthData(null, null);

        assertThrows(DataAccessException.class, () -> auth.createAuth(testAuthData), "Not Thrown");
    }

    // Shows that data persists when server stops/restarts and that getAuth can successfully retrieve it
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

    //Shows that MissingDataException is thrown when getAuth is passed null AuthToken
    @Test
    @Order(9)
    @DisplayName("Unsuccessful getAuth")
    void unsuccessfulGetAuth(){

        assertThrows(MissingDataException.class, () -> auth.getAuth(null), "Not Thrown");
    }

    //Shows that remove auth successfully removes records from auth table
    @Test
    @Order(10)
    @DisplayName("Successful Remove Auth")
    void successfulRemove() throws DataAccessException {

        assertEquals(authData, auth.getAuth(authData.authToken()), "AuthData not stored correctly");

        auth.remove(authData.authToken());

        assertNull(auth.getAuth(authData.authToken()), "AuthData not removed");
    }

    //shows that DataAccessException is thrown when null authToken is passed to removeAuth
    @Test
    @Order(11)
    @DisplayName("Unsuccessful Remove Auth")
    void unsuccessfulRemove() {

        assertThrows(DataAccessException.class, () -> auth.remove(null), "Not Thrown");
    }

    //shows that auth table is successfully truncated with clear call in MySqlAuthDAO
    @Test
    @Order(12)
    @DisplayName("Successful Clear Auth Table")
    void clearAuthTable() throws DataAccessException {

        AuthData testAuth = new AuthData("fakeAuthToken", "FakeUsername");

        auth.createAuth(testAuth);

        assertNotNull(auth.getAuth(testAuth.authToken()), "AuthToken not stored correctly");

        assertNotNull(auth.getAuth(authData.authToken()), "AuthToken not stored correctly");

        auth.clear();

        assertNull(auth.getAuth(testAuth.authToken()), "AuthData not cleared");

        assertNull(auth.getAuth(authData.authToken()), "AuthData not cleared");
    }

    //Shows that createGame successfully stores gameData in database, and it persists when server stops/restarts
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

    //Shows that DataAccessException is thrown when createGame receives a null game name
    @Test
    @Order(14)
    @DisplayName("Unsuccessful Create Game")
    void unsuccessfulCreateGame() {

        assertThrows(DataAccessException.class, () -> game.createGame(null), "Not Thrown");
    }


    /*Shows that getGame successfully retrieves game data from game
    table and that data persists when server stops/restarts
     */
    @Test
    @Order(15)
    @DisplayName("Successful getGame")
    void successfulGetGame() throws DataAccessException {

        server.stop();

        server.run(0);

        GameData storedGameData = game.getGame(1);

        assertEquals(gameName, storedGameData.gameName(), "Correct Game Not Retrieved");
    }

    //Shows that MissingDataException is thrown when gameID = 0 is passed into getGame
    @Test
    @Order(16)
    @DisplayName("Unsuccessful getGame")
    void unsuccessfulGetGame() {

        assertThrows(MissingDataException.class, () -> game.getGame(0), "Not Thrown");
    }

    //Shows that listGames successfully returns a list of all games and that data persists when server stop/restarts
    @Test
    @Order(17)
    @DisplayName("Successful listGames")
    void successfulListGames() throws DataAccessException {

        ArrayList<ListGameData> testList = game.listGames(authData.authToken());

        assertNotNull(testList, "ListGames returned null");

        server.stop();

        server.run(0);

        ArrayList<ListGameData> testPersistList = game.listGames(authData.authToken());

        assertNotNull(testPersistList, "ListGames returned null");

        assertEquals(testList, testPersistList, "Data didn't properly persist in storage");
    }

    //Shows that MissingDataException is thrown when null authToken is passed into listGames
    @Test
    @Order(18)
    @DisplayName("Unsuccessful listGames")
    void unsuccessfulListGames() {

        assertThrows(MissingDataException.class, () -> game.listGames(null), "Not Thrown");
    }

    /*Shows that joinGame successfully updates an existing game with a new gameName and black or white username
    and that the stored data and updates persist when server stops/restarts
     */
    @Test
    @Order(19)
    @DisplayName("Successful joinGame")
    void successfulJoinGame() throws DataAccessException {

        GameData updatedGame = game.joinGame(1, userData.username(), "WHITE", gameData.gameName());

        assertEquals(newGameData.blackUsername(), updatedGame.blackUsername(), "blackUser not stored correctly");

        assertEquals(updatedGame.whiteUsername(), userData.username(), "Game did not update");

        assertEquals(gameData.gameID(), updatedGame.gameID(), "Game incorrectly changed gameID");

        assertEquals(updatedGame.gameName(), gameData.gameName(), "gameName not updated");

        server.stop();

        server.run(0);

        assertEquals(updatedGame.whiteUsername(), userData.username(), "Data did not persist");

        assertEquals(gameData.gameID(), updatedGame.gameID(), "Data did not persist");

        assertEquals(updatedGame.gameName(), gameData.gameName(), "Data did not persist");
    }

    //Shows that AlreadyTakenException is thrown when user tries to joinGame where another user is already playing
    @Test
    @Order(20)
    @DisplayName("Unsuccessful joinGame")
    void unsuccessfulJoinGame() {

        assertThrows(AlreadyTakenException.class, () -> game.joinGame(1,
                "fake", "BLACK", "test2"), "Not Thrown");
    }

    //Shows that game table truncates when clear is called in MySqlGameDAO
    @Test
    void clearGameTable() throws DataAccessException {

        game.createGame("test2");

        assertNotNull(game.getGame(1), "Game not stored correctly");

        assertNotNull(game.getGame(2), "Game not stored correctly");

        game.clear();

        assertNull(game.getGame(1), "Game table not truncated");

        assertNull(game.getGame(2), "Game table not truncated");

    }
}
