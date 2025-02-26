package handlers;

import service.ListGamesService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.InvalidCredentialsException;
import response.ListGamesResponse;
import spark.Request;
import spark.Response;

/*
    Class used to Handle ListGame Requests, call ListGameService, and to
    return ListGameResponse objects or errors based on results of calling ListGameService
 */

public class ListGamesHandler extends BaseHandler{

    /*initializes MemoryAuthDAO and MemoryGameDAO objects to make calling their
    class methods easier
    */
    private MemoryAuthDAO auth;
    private MemoryGameDAO game;

    public ListGamesHandler(MemoryAuthDAO auth, MemoryGameDAO game) {
        this.auth = auth;
        this.game = game;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String authToken = request.headers("Authorization");


        /*checks to see if authToken is null or absent from map storage. If so, sets status at 401 and returns json
        of InvalidCredentialsException that's thrown along with its message
         */
        if (authToken == null || auth.getAuth(authToken) == null) {

            response.status(401);

            String jsonResp = new ErrorFormatter(new InvalidCredentialsException("Error: unauthorized")).getErrorFormat();

            return jsonResp;
        }

         /*Attempts to call listGames from ListGamesService. If successful, sets status to 200 and returns
        json of resulting ListGamesResponse object.
         */
        try{

            ListGamesService listService = new ListGamesService();

            ListGamesResponse resp = listService.listGames(authToken, auth, game);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

             /*catches DataAccessException thrown by ListGamesService, sets status to 500, and returns json
            of Exception and message
             */
        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
