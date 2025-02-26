package handlers;

import service.CreateGameService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.InvalidCredentialsException;
import exceptions.MissingDataException;
import request.CreateGameRequest;
import response.CreateGameResponse;
import spark.Request;
import spark.Response;

/*
    Class used to Handle CreateGame Requests, call CreateGameService, and to
    return CreateGameResponse objects or errors based on results of calling CreateGameService
 */

public class CreateGameHandler extends BaseHandler {

    /*initializes MemoryAuthDAO and MemoryGameDAO objects to make calling their
    class methods easier
    */
    private MemoryGameDAO game;
    private MemoryAuthDAO auth;

    public CreateGameHandler(MemoryGameDAO game, MemoryAuthDAO auth) {
        this.game = game;
        this.auth = auth;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        CreateGameRequest req = gson.fromJson(request.body(), CreateGameRequest.class);

        String authToken = request.headers("Authorization");

        /*checks to see if authToken is null or absent from map storage. If so, sets status at 401 and returns json
        of InvalidCredentialsException that's thrown along with its message
         */
        if (authToken == null || auth.getAuth(authToken) == null) {

            response.status(401);

            String jsonResp = new ErrorFormatter(new InvalidCredentialsException("Error: unauthorized")).getErrorFormat();

            return jsonResp;
        }

        /*Attempts to call createGame from CreateGameService. If successful, sets status to 200 and returns
        json of resulting CreateGameResponse object.
         */
        try {

            CreateGameService createGame = new CreateGameService();

            CreateGameResponse resp = createGame.createGame(req, game);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

            /*if call to createGame results in MissingDataException, catches the exception, sets status at 400, and
            returns json of the MissingDataException and its message.
             */
        } catch (MissingDataException e) {

            response.status(400);

            String jsonResp = new ErrorFormatter(new MissingDataException(e.getMessage())).getErrorFormat();

            return jsonResp;

            /*catches DataAccessException thrown by CreateGameService, sets status to 500, and returns json
            of Exception and message
             */
        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
