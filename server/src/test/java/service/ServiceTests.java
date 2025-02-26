package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.InvalidCredentialsException;
import exceptions.MissingDataException;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    private static MemoryAuthDAO auth;
    private static MemoryUserDAO user;
    private static String password;
    private static MemoryGameDAO game;
    private static RegisterResponse resp;


    /* Before each test, this makes sure that there is a MemoryAuthDAO object, a MemoryUserDAO object,
    a MemoryGameDAO object, a username, a password, and an authToken that can be used to ensure valid
    credentials in each test
     */
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


    /*
    Test designed to confirm RegisterService is able to successfully return a RegisterResponse object with correct data,
    enters the new UserData in MemoryUserDAO, and enters the new AuthData in MemoryAuthDAO
     */
    @Test
    @Order(1)
    @DisplayName("Successful Registration")
    void successfulRegister() throws Exception {

        RegisterRequest req = new RegisterRequest("username", "password", "email");

        RegisterService registerService = new RegisterService();

        RegisterResponse response = registerService.register(req, user, auth);

        assertNotNull(user.getUser(req.username()), "username not stored in Map/Database");

        assertNotNull(response, "RegisterResponse returned null");

        assertEquals(req.username(), response.username(), "usernames don't match");

        assertNotNull(response.authToken(), "authToken in RegisterResponse returned null");

        assertNotNull(auth.getAuth(response.authToken()), "AuthData not stored in Map/Database");

    }


    /*
    Test designed to confirm that RegisterService throws a MissingDataException if one of the attributes of
    RegisterRequest is null
     */
    @Test
    @Order(2)
    @DisplayName("Registration Bad Request")
    void badRequestRegister(){

        RegisterRequest req = new RegisterRequest(null, "password", "email");

        RegisterService registerService = new RegisterService();

        assertThrows(MissingDataException.class, () -> registerService.register(req, user, auth),"Not Thrown");

    }


    /*
     Test designed to confirm that LoginService successfully returns a LoginResponse object with the correct data
     and that an authToken is created for the user and stored in the MemoryAuthDAO map
    */
    @Test
    @Order(3)
    @DisplayName("Successful Login")
    void successfulLogin() throws Exception {

        LoginRequest req = new LoginRequest(resp.username(), password);

        LoginService loginService = new LoginService();

        LoginResponse response = loginService.login(req, user, auth);

        assertNotNull(response, "LoginResponse returned null");

        assertEquals(response.username(), resp.username(), "Response username doesn't equal login username");

        assertNotNull(response.authToken(), "LoginResponse authToken is null");

        assertNotNull(auth.getAuth(response.authToken()), "AuthData not stored in Map/Database");

    }


    /*
    Test designed to confirm that LoginService throws a InvalidCredentialsException when a user enters a password
    that is incorrect for a given username
     */
    @Test
    @Order(4)
    @DisplayName("Login Unauthorized")
    void unauthorizedLogin() {

        LoginRequest req = new LoginRequest(resp.username(), "badPassword");

        LoginService loginService = new LoginService();

        assertThrows(InvalidCredentialsException.class,() -> loginService.login(req, user, auth),"Not Thrown");

    }

    /*
    Test designed to confirm that LogoutService returns a valid LogoutResponse object and that AuthData doesn't
    persist in MemoryAuthDAO after a user logs out
     */
    @Test
    @Order(5)
    @DisplayName("Successful Logout")
    void successfulLogout() throws DataAccessException {

        LogoutService logoutService = new LogoutService();

        LogoutResponse response = logoutService.logout(resp.authToken(), auth);

        assertNotNull(response, "LogoutResponse is null");

        assertNull(auth.getAuth(resp.authToken()), "AuthData still in Map/Database");

    }

    /*
    Test designed to confirm that LogoutService throws an InvalidCredentialsException when given an authtoken
    that isn't valid/in MemoryAuthDAO
     */
    @Test
    @Order(6)
    @DisplayName("Logout Unauthorized")
    void unauthorizedLogout(){

        LogoutService logoutService = new LogoutService();

        assertThrows(InvalidCredentialsException.class, ()-> logoutService.logout("badToken", auth));

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
