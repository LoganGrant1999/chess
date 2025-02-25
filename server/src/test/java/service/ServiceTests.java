package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.MissingDataException;
import org.junit.jupiter.api.*;
import request.RegisterRequest;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    @Order(1)
    @DisplayName("Successful Registration")
    void successfulRegister() throws Exception {

        RegisterRequest req = new RegisterRequest("username", "password", "email");

        MemoryUserDAO user = new MemoryUserDAO();

        MemoryAuthDAO auth = new MemoryAuthDAO();

        RegisterService registerService = new RegisterService();

        RegisterResponse resp = registerService.register(req, user, auth);

        assertNotNull(user.getUser(req.username()));

        assertNotNull(resp, "RegisterResponse returned null");

        assertEquals(req.username(), resp.username(), "username provided didn't match the username in the RegisterResponse");

        assertNotNull(resp.authToken(), "authToken in RegisterResponse returned null");

    }


    @Test
    @Order(2)
    @DisplayName("Registration Bad Request")
    void badRequestRegister() throws Exception {

        RegisterRequest req = new RegisterRequest(null, "password", "email");

        MemoryUserDAO user = new MemoryUserDAO();

        MemoryAuthDAO auth = new MemoryAuthDAO();

        RegisterService registerService = new RegisterService();

        assertThrows(MissingDataException.class, () ->  registerService.register(req, user, auth), "MissingDataException Not Thrown when username is null");

    }





    @Test
    void login() {
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
