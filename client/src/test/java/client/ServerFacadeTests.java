package client;

import exceptions.NetworkException;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;
import server.Server;
import server.ServerFacade;

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
    @Order(1)
    @DisplayName("Successful Registration")
    public void successfulRegistration() throws NetworkException {

        RegisterRequest req = new RegisterRequest("test", "test", "test@test.com");

        RegisterResponse response = facade.register(req);

        assertNotNull(response.username());

        assertNotNull(response.authToken());

        assertTrue(response.authToken().length() > 10);
    }

    @Test
    @Order(2)
    @DisplayName("Unsuccessful Registration")
    public void unsuccessfulRegistration() {

        RegisterRequest request = new RegisterRequest(null, null, null);

        assertThrows(NetworkException.class, () -> facade.register(request), "Not Thrown");
    }

    @Test
    @Order(3)
    @DisplayName("Successful Login")
    public void successfulLogin() throws NetworkException {

        LoginRequest request = new LoginRequest(req.username(), req.password());

        LoginResponse response = facade.login(request);

        assertNotNull(response.authToken());

        assertTrue(response.authToken().length() > 10);

        assertNotNull(response.username());
    }

    @Test
    @Order(4)
    @DisplayName("Unsuccessful Login")
    public void unsuccessfulLogin(){

        LoginRequest request = new LoginRequest(null, null);

        assertThrows(NetworkException.class, () -> facade.login(request));
    }

    @Test
    @Order(5)
    @DisplayName("Successful Logout")
    public void successfulLogout() throws NetworkException {

        LogoutResponse response = assertDoesNotThrow(() -> facade.logout(resp.authToken()));

        assertNull(response);
    }

    @Test
    @Order(6)
    @DisplayName("Unsuccessful Logout")
    public void unsuccessfulLogout(){

        assertThrows(NetworkException.class, () -> facade.logout(null));
    }




}
