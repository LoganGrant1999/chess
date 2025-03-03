package handlers;

import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.InvalidCredentialsException;
import exceptions.MissingDataException;
import request.JoinGameRequest;
import response.JoinGameResponse;
import service.JoinGameService;
import spark.Request;
import spark.Response;

/*
    Class used to Handle JoinGame Requests, call JoinGameService, and to
    return JoinGameResponse objects or errors based on results of calling JoinGameService
 */

public class JoinGameHandler extends BaseHandler{

    /*initializes AuthDAO, GameDAO, and UserDAO objects to make calling their
    class methods easier
    */
    private UserDAO user;
    private AuthDAO auth;
    private GameDAO game;

    public JoinGameHandler(UserDAO user, AuthDAO auth, GameDAO game) {
        this.user = user;
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

        JoinGameRequest req = gson.fromJson(request.body(), JoinGameRequest.class);

        /*Attempts to call joinGame from JoinGameService. If successful, sets status to 200 and returns
        json of resulting JoinGameResponse object.
         */
        try {

            JoinGameService joinService = new JoinGameService();

            JoinGameResponse resp = joinService.joinGame(req, user, auth, game, authToken);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;


            /*if call to joinGame results in MissingDataException, catches the exception, sets status at 400, and
            returns json of the MissingDataException and its message.
             */
        } catch (MissingDataException e) {

            response.status(400);

            String jsonResp = new ErrorFormatter(new MissingDataException(e.getMessage())).getErrorFormat();

            return jsonResp;

            /*if call to joinGame results in AlreadyTakenException, catches the exception, sets status to 403, and
            returns json of the AlreadyTakenException and its message
             */
        } catch (AlreadyTakenException e){

            response.status(403);

            String jsonResp = new ErrorFormatter(new AlreadyTakenException(e.getMessage())).getErrorFormat();

            return jsonResp;

             /*catches DataAccessException thrown by JoinGameService, sets status to 500, and returns json
            of Exception and message
             */
        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
