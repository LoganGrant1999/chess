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

            String jsonResp = new ErrorFormatter(new InvalidCredentialsException("Error: unauthorized")).getErrorFormat();

            return jsonResp;
        }

        JoinGameRequest req = gson.fromJson(request.body(), JoinGameRequest.class);

        try {

            JoinGameService joinService = new JoinGameService();

            JoinGameResponse resp = joinService.joinGame(req, user, auth, game, authToken);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (MissingDataException e) {

            response.status(400);

            String jsonResp = new ErrorFormatter(new MissingDataException(e.getMessage())).getErrorFormat();

            return jsonResp;

        } catch (AlreadyTakenException e){

            response.status(403);

            String jsonResp = new ErrorFormatter(new AlreadyTakenException(e.getMessage())).getErrorFormat();

            return jsonResp;

        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
