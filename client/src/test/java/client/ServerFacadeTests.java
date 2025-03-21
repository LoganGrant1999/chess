package client;

import exceptions.NetworkException;
import org.junit.jupiter.api.*;
import request.RegisterRequest;
import response.RegisterResponse;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

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
    }

    @Test
    @Order(1)
    @DisplayName("Successful Registration")
    public void successfulRegistration() throws NetworkException {

        RegisterRequest req = new RegisterRequest("test", "test", "test@test.com");

        RegisterResponse resp = facade.register(req);

        assertNotNull(resp.username());

        assertNotNull(resp.authToken());

        assertTrue(resp.authToken().length() > 10);
    }




}
