package service;


import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.InvalidCredentialsException;
import exceptions.MissingDataException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
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
    @Order(5)
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
    @Order(6)
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
    @Order(7)
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
    @Order(8)
    @DisplayName("CreateGame Bad Request")
    void badRequestCreateGame(){


        CreateGameRequest req = new CreateGameRequest(null);


        CreateGameService createGameService = new CreateGameService();


        assertThrows(MissingDataException.class, ()-> createGameService.createGame(req, game), "Not thrown");


    }


    /*
    Test is designed to confirm that ListGamesService returns a valid/not null ListGamesResponse object
     */
    @Test
    @Order(9)
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
    @Order(10)
    @DisplayName("ListGames Unauthorized")
    void unauthorizedListGames(){


        ListGamesService listGamesService = new ListGamesService();


        assertThrows(InvalidCredentialsException.class,()->
                listGamesService.listGames("1",auth,game),"Not Thrown");
    }


    /*
    This test is designed to confirm that JoinGame returns a valid JoinGameResponse Object, and that the
    user who is joining the game successfully has their username stored in the game in MemoryGameDAO under
    the color that they indicated they wanted to assume
     */
    @Test
    @Order(11)
    @DisplayName("Successful JoinGame")
    void successfulJoinGame() throws Exception {


        JoinGameRequest req = new JoinGameRequest("WHITE", gameResp.gameID());


        JoinGameService joinGameService = new JoinGameService();


        JoinGameResponse response = joinGameService.joinGame(req, user, auth, game, resp.authToken());


        AuthData authData = auth.getAuth(resp.authToken());


        String username = authData.username();


        GameData gameData = game.getGame(gameResp.gameID());


        String whiteUsername = gameData.whiteUsername();


        assertNotNull(response);


        assertEquals(username, whiteUsername);


    }




    /*
    Test is designed to confirm that a MissingDataException is thrown when the JoinGameRequest object has a null
    value for its playerColor attribute
     */
    @Test
    @Order(12)
    @DisplayName("JoinGame Bad Request")
    void badRequestJoinGame(){


        JoinGameRequest req = new JoinGameRequest(null, gameResp.gameID());


        JoinGameService joinGameService = new JoinGameService();


        assertThrows(MissingDataException.class,
                ()->joinGameService.joinGame(req, user, auth, game, resp.authToken()), "Not Thrown");
    }


    /*
    Test is designed to confirm that ClearService successfully returns a ClearResponse object when called
     */
    @Test
    @Order(13)
    @DisplayName("Successful Clear")
    void clearAllData() throws Exception {


        RegisterService registerService = new RegisterService();
        ListGamesService listGamesService = new ListGamesService();


        RegisterRequest req1 = new RegisterRequest("user1", password, "email");
        RegisterRequest req2 = new RegisterRequest("user2", password, "email");


        RegisterResponse resp1 = registerService.register(req1, user, auth);
        RegisterResponse resp2 = registerService.register(req2, user, auth);


        new CreateGameRequest("game2");
        new CreateGameRequest("game3");


        ClearService clearService = new ClearService();


        ClearResponse response = clearService.clearAllData(auth, game, user);


        assertNotNull(response);


        assertNull(auth.getAuth(resp.authToken()), "MemoryAuthDAO map not cleared");


        assertNull(auth.getAuth(resp1.authToken()), "MemoryAuthDAO map not cleared");


        assertNull(auth.getAuth(resp2.authToken()), "MemoryAuthDAO map not cleared");


        assertNull(user.getUser(resp.username()), "MemoryUserDAO map not cleared");


        assertNull(user.getUser(resp1.username()), "MemoryUserDAO map not cleared");


        assertNull(user.getUser(resp2.username()), "MemoryUserDAO map not cleared");


        assertThrows(InvalidCredentialsException.class,
                ()->listGamesService.listGames(resp.authToken(),auth, game), "GameDAO not cleared");


        assertThrows(InvalidCredentialsException.class,
                ()->listGamesService.listGames(resp1.authToken(),auth, game), "GameDAO not cleared");


        assertThrows(InvalidCredentialsException.class,
                ()->listGamesService.listGames(resp2.authToken(),auth, game), "GameDAO not cleared");
    }


}
