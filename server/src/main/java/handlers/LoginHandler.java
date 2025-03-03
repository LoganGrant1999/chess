package handlers;

import dataaccess.*;
import service.LoginService;
import exceptions.InvalidCredentialsException;
import request.LoginRequest;
import response.LoginResponse;
import spark.Request;
import spark.Response;


/*
    Class used to Handle Login Requests, call LoginService, and to
    return LoginResponse objects or errors based on results of calling LoginService
 */

public class LoginHandler extends BaseHandler{

    /*initializes AuthDAO and GameDAO objects to make calling their
    class methods easier
    */
    private UserDAO user;
    private AuthDAO auth;

    public LoginHandler(UserDAO user, AuthDAO auth) {
        this.user = user;
        this.auth = auth;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        LoginRequest req = gson.fromJson(request.body(), LoginRequest.class);

        /*Attempts to call login from LoginService. If successful, sets status to 200 and returns
        json of resulting LoginResponse object.
         */
        try{

            LoginService logService = new LoginService();

            LoginResponse resp = logService.login(req, user, auth);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

            /* if InvalidCredentialsException is thrown from calling login method,  catches it,
            sets status to 401 and returns json of InvalidCredentialsException and its message
             */
        } catch (InvalidCredentialsException e){

            response.status(401);

            String jsonResp = new ErrorFormatter(new InvalidCredentialsException("Error: unauthorized")).getErrorFormat();

            return jsonResp;

            /*
            catches DataAccessException thrown by ListGamesService, sets status to 500, and returns json
            of Exception and message
             */
        }  catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
