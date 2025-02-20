package handlers;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.AlreadyTakenException;
import exceptions.InvalidCredentialsException;
import exceptions.MissingDataException;
import request.JoinGameRequest;
import response.JoinGameResponse;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends BaseHandler{

    private MemoryUserDAO user;
    private MemoryAuthDAO auth;
    private MemoryGameDAO game;

    public JoinGameHandler(MemoryUserDAO user, MemoryAuthDAO auth, MemoryGameDAO game) {
        this.user = user;
        this.auth = auth;
        this.game = game;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String authToken = request.headers("Authorization");

        if (authToken == null || auth.getAuth(authToken) == null) {

            response.status(401);

            ErrorFormatter errorResp= new ErrorFormatter(new InvalidCredentialsException("Error: unauthorized"));

            String jsonResp = gson.toJson(errorResp.getErrorFormat());

            return jsonResp;
        }

        JoinGameRequest req = gson.fromJson(request.body(), JoinGameRequest.class);

        try {

            JoinGameService joinService = new JoinGameService();

            JoinGameResponse resp = joinService.join(req, user, auth, game, authToken);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (MissingDataException e) {

            response.status(400);

            return new ErrorFormatter(e).getErrorFormat();

        } catch (AlreadyTakenException e){

            response.status(403);

            return new ErrorFormatter(e).getErrorFormat();

        } catch (DataAccessException e){

            response.status(500);

            return new ErrorFormatter(e).getErrorFormat();
        }
    }
}
