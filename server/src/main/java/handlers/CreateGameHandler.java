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

public class CreateGameHandler extends BaseHandler {

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

        if (authToken == null || auth.getAuth(authToken) == null) {

            response.status(401);

            String jsonResp = new ErrorFormatter(new InvalidCredentialsException("Error: unauthorized")).getErrorFormat();

            return jsonResp;
        }

        try {

            CreateGameService createGame = new CreateGameService();

            CreateGameResponse resp = createGame.create(req, game);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (MissingDataException e) {

            response.status(400);

            String jsonResp = new ErrorFormatter(new MissingDataException(e.getMessage())).getErrorFormat();

            return jsonResp;

        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;

        }
    }
}
