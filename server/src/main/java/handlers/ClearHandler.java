package handlers;

import dataaccess.*;
import response.ClearResponse;
import service.ClearService;
import spark.Request;
import spark.Response;

/*
    Class used to Handle Clear Requests, call ClearService, and to
    return ClearResponse objects or errors based on results of calling ClearService
 */

public class ClearHandler extends BaseHandler{

    /*initializes AuthDAO, GameDAO, and UserDAO objects to make calling their
    class methods easier
    */

    private AuthDAO auth;
    private GameDAO game;
    private UserDAO user;


    public ClearHandler(AuthDAO auth, GameDAO game, UserDAO user) {
        this.auth = auth;
        this.game = game;
        this.user = user;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        /*Attempts to call clearService. If there are no exceptions thrown,
        returns json version of ClearResponse object returned and sets status at 200
         */
        try {

            ClearService clearService = new ClearService();

            ClearResponse resp = clearService.clearAllData(auth, game, user);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

            /*catches DataAccessException thrown by clearService, sets status to 500, and returns json
            of Exception and message
             */
        } catch (DataAccessException e){

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
