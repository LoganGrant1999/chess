package handlers;

import service.LogoutService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.InvalidCredentialsException;
import response.LogoutResponse;
import spark.Request;
import spark.Response;

public class LogoutHandler extends BaseHandler{

    //initializes MemoryAuthDAO object to make calling its class methods easier

    private MemoryAuthDAO auth;

    public LogoutHandler(MemoryAuthDAO auth) {
        this.auth = auth;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String authToken = request.headers("Authorization");


        /*Attempts to call logout from LoginService. If successful, sets status to 200 and returns
        json of resulting LogoutResponse object.
         */
        try {

            LogoutService logService = new LogoutService();

            LogoutResponse resp = logService.logout(authToken, auth);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

            /*
            catches InvalidCredentialsException thrown by ListGamesService, sets status to 500, and returns json
            of Exception and message
             */
        } catch(InvalidCredentialsException e){

            response.status(401);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;

            /*
            catches DataAccessException thrown by ListGamesService, sets status to 500, and returns json
            of Exception and message
             */
        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
