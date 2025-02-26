package handlers;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import response.ClearResponse;
import service.ClearService;
import spark.Request;
import spark.Response;

/*
    Class used to Handle Clear Requests, call ClearService, and to
    return ClearResponse objects or errors based on results of calling ClearService
 */

public class ClearHandler extends BaseHandler{

    /*initializes MemoryAuthDAO, MemoryGameDAO, and MemoryUserDAO objects to make calling their
    class methods easier
    */

    private MemoryAuthDAO auth;
    private MemoryGameDAO game;
    private MemoryUserDAO user;


    public ClearHandler(MemoryAuthDAO auth, MemoryGameDAO game, MemoryUserDAO user) {
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
