package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.MissingDataException;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    private static MemoryAuthDAO auth;
    private static MemoryUserDAO user;
    private static String password;
    private static MemoryGameDAO game;
    private static RegisterResponse resp;


    @BeforeAll
    public static void setup() throws Exception {

        auth = new MemoryAuthDAO();

        user = new MemoryUserDAO();

        game = new MemoryGameDAO();

        password = "password";

        RegisterRequest req = new RegisterRequest("testUser", "password", "email");

        RegisterService registerService = new RegisterService();

        resp = registerService.register(req, user, auth);

    }


    @Test
    @Order(1)
    @DisplayName("Successful Registration")
    void successfulRegister() throws Exception {

        RegisterRequest req = new RegisterRequest("username", "password", "email");

        RegisterService registerService = new RegisterService();

        RegisterResponse response = registerService.register(req, user, auth);

        assertNotNull(user.getUser(req.username()));

        assertNotNull(response, "RegisterResponse returned null");

        assertEquals(req.username(), response.username(), "username provided didn't match the username in the RegisterResponse");

        assertNotNull(response.authToken(), "authToken in RegisterResponse returned null");

    }


    @Test
    @Order(2)
    @DisplayName("Registration Bad Request")
    void badRequestRegister(){

        RegisterRequest req = new RegisterRequest(null, "password", "email");

        RegisterService registerService = new RegisterService();

        assertThrows(MissingDataException.class, () -> registerService.register(req, user, auth),"Not Thrown");

    }


    @Test
    @Order(3)
    @DisplayName("Successful Login")
    void login() throws Exception {

        LoginRequest req = new LoginRequest(resp.username(), password);

        LoginService loginService = new LoginService();

        LoginResponse response = loginService.login(req, user, auth);

        assertNotNull(response, "LoginResponse returned null");

        assertEquals(response.username(), resp.username(), "Response username doesn't equal login username");

        assertNotNull(response.authToken(), "LoginResponse authToken is null");

        assertNotNull(auth.getAuth(response.username()), "AuthToken not stored");

    }


    @Test
    void logout() {
    }

    @Test
    void createGame() {
    }


    @Test
    void listGames() {
    }


    @Test
    void joinGame() {
    }


    @Test
    void clearAllData() {
    }

}
