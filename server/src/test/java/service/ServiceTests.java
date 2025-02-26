package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.InvalidCredentialsException;
import exceptions.MissingDataException;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    //instantiate static variables so that setup can access them and can be used to fill data needs in tests
    private static MemoryAuthDAO auth;
    private static MemoryUserDAO user;
    private static String password;
    private static MemoryGameDAO game;
    private static RegisterResponse resp;
    private static CreateGameResponse gameResp;

    /* Before each test, this makes sure that there is a MemoryAuthDAO object, a MemoryUserDAO object,
    a MemoryGameDAO object, a username, a password, and an authToken that can be used to ensure valid
    credentials in each test
     */
    @BeforeEach
    public void setup() throws Exception {

        auth = new MemoryAuthDAO();

        user = new MemoryUserDAO();

        game = new MemoryGameDAO();

        password = "password";

        RegisterRequest req = new RegisterRequest("testUser", "password", "email");

        RegisterService registerService = new RegisterService();

        resp = registerService.register(req, user, auth);

        CreateGameRequest gameReq = new CreateGameRequest("game1");

        CreateGameService createGameService = new CreateGameService();

        gameResp = createGameService.createGame(gameReq, game);

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

        assertEquals(response.username(), req.username(), "Response username doesn't equal login username");

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
    @Order(9)
    @DisplayName("Successful Logout")
    void successfulLogout() throws DataAccessException {

        LogoutService logoutService = new LogoutService();

        LogoutResponse response = logoutService.logout(resp.authToken(), auth);

        assertNotNull(response, "LogoutResponse is null");

        assertNull(auth.getAuth(resp.authToken()), "AuthData still in Map/Database");

    }

    /*
    Test designed to confirm that LogoutService throws an InvalidCredentialsException when given an authToken
    that isn't valid/in MemoryAuthDAO
     */
    @Test
    @Order(5)
    @DisplayName("Logout Unauthorized")
    void unauthorizedLogout(){

        LogoutService test = new LogoutService();

        assertThrows(InvalidCredentialsException.class,()-> test.logout("bad", auth),"Not thrown");

    }


    /*
    Test designed to confirm that CreateGameService successfully creates a CreateGameResponse object with
    valid data, and that it stores the new GameData in the MemoryGameDAO map
     */
    @Test
    @Order(6)
    @DisplayName("Successful CreateGame")
    void successfulCreateGame() throws DataAccessException {

        CreateGameRequest req = new CreateGameRequest("test");

        CreateGameService createGameService = new CreateGameService();

        CreateGameResponse response = createGameService.createGame(req, game);

        assertNotNull(response, "CreateGameResponse returned null");

        assertNotEquals(0, response.gameID(), "gameId returned as 0/null");

        assertNotNull(game.getGame(response.gameID()), "GameData not stored");

    }

    /*
    Test designed to confirm that CreateGameService throws a MissingDataException when given a null
    gameName in the CreateGameRequest
     */
    @Test
    @Order(7)
    @DisplayName("Create Game Bad Request")
    void badRequestCreateGame(){

        CreateGameRequest req = new CreateGameRequest(null);

        CreateGameService createGameService = new CreateGameService();

        assertThrows(MissingDataException.class, ()-> createGameService.createGame(req, game), "Not thrown");

    }

    /*
    Test is designed to confirm that ListGamesService returns a valid/not null ListGamesResponse object
     */
    @Test
    @Order(8)
    @DisplayName("Successful ListGames")
    void successfulListGames() throws DataAccessException {

        ListGamesService listGamesService = new ListGamesService();

        ListGamesResponse response = listGamesService.listGames(resp.authToken(), auth, game);

        assertNotNull(response, "ListGamesResponse returned null");

        assertNotNull(response.games(), "The ArrayList of games in ListGamesResponse returned null");

    }

    /*
    Test designed to confirm that ListGames throws an InvalidCredentialsException when ListGamesRequest contains
    and invalid authToken
     */
    @Test
    @Order(9)
    @DisplayName("ListGames Unauthorized")
    void unauthorizedListGames(){

        ListGamesService test = new ListGamesService();

        assertThrows(InvalidCredentialsException.class,()->test.listGames("1",auth,game),"No Throw");
    }


    @Test
    void joinGame() {

    }


    @Test
    void clearAllData() {
    }

}
