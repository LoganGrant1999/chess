package dataaccess;

import handlers.ClearHandler;
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

    @BeforeEach
    public void setup() throws Exception {

        auth = new MySqlAuthDAO();

        game = new MySqlGameDAO();

        user = new MySqlUserDAO();

        server = new Server();

        server.run(0);

        DatabaseManager.configureDatabase();

        clearer = new ClearService();

        clearer.clearAllData(auth, game, user);

        DatabaseManager.configureDatabase();

    }


    @AfterEach
    public void stopServer(){

        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Successful Create User Test")
    void successfulCreateUser() throws DataAccessException {

        UserData userData = new UserData("username", "password", "email@email.com");

        user.createUser(userData);

        server.stop();

        server.run(0);

        UserData storedUserData = user.getUser(userData.username());

        assertEquals(userData.username(), storedUserData.username(), "Username not stored correctly");

        assertEquals(userData.email(), storedUserData.email(), "Email not stored correctly");

        assertTrue(BCrypt.checkpw(userData.password(), storedUserData.password()));
    }


    @Test
    @Order(2)
    @DisplayName("Unsuccessful Create User Test")
    void unsuccessfulCreateUser() {

        UserData userData = new UserData(null, "password", "email");

        assertThrows(DataAccessException.class, () -> user.createUser(userData), "Not Thrown");

    }











    @Test
    void getUser() {
    }

    @Test
    void clear() {
    }
}
