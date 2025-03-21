package client;

import exceptions.NetworkException;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import server.Server;
import server.ServerFacade;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    private static RegisterRequest req;

    private static RegisterResponse resp;

    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    public void clearDB() throws NetworkException {

        facade.clear();

        req = new RegisterRequest("testUser", "password", "email");

        resp =  facade.register(req);
    }

    @Test
    @DisplayName("Successful Registration")
    public void successfulRegistration() throws NetworkException {

        RegisterRequest req = new RegisterRequest("test", "test", "test@test.com");

        RegisterResponse response = facade.register(req);

        assertNotNull(response.username());

        assertNotNull(response.authToken());

        assertTrue(response.authToken().length() > 10);
    }

    @Test
    @DisplayName("Unsuccessful Registration")
    public void unsuccessfulRegistration() {

        RegisterRequest request = new RegisterRequest(null, null, null);

        assertThrows(NetworkException.class, () -> facade.register(request), "Not Thrown");
    }

    @Test
    @DisplayName("Successful Login")
    public void successfulLogin() throws NetworkException {

        LoginRequest request = new LoginRequest(req.username(), req.password());

        LoginResponse response = facade.login(request);

        assertNotNull(response.authToken());

        assertTrue(response.authToken().length() > 10);

        assertNotNull(response.username());
    }

    @Test
    @DisplayName("Unsuccessful Login")
    public void unsuccessfulLogin(){

        LoginRequest request = new LoginRequest(null, null);

        assertThrows(NetworkException.class, () -> facade.login(request));
    }

    @Test
    @DisplayName("Successful Logout")
    public void successfulLogout() throws NetworkException {

        LogoutResponse response = assertDoesNotThrow( () -> facade.logout(resp.authToken()));

        assertNull(response);
    }

    @Test
    @DisplayName("Unsuccessful Logout")
    public void unsuccessfulLogout(){

        assertThrows(NetworkException.class, () -> facade.logout(null));
    }

    @Test
    @DisplayName("Successful Create Game")
    public void successfulCreateGame() throws NetworkException {

        CreateGameRequest request = new CreateGameRequest("testGame");

        CreateGameResponse response = facade.createGame(request, resp.authToken());

        assertNotNull(response.gameID());
    }

    @Test
    @DisplayName("Unsuccessful Create Game")
    public void unsuccessfulCreateGame() {

        assertThrows(NetworkException.class, () -> facade.createGame(null, null));
    }

    @Test
    @DisplayName("Successful List Games")

    public void successfulListGames() throws NetworkException {

        ListGamesResponse response = assertDoesNotThrow( () ->  facade.listGames(resp.authToken()));

        assertNotNull(response);

    }


}
