package handlers;

import Service.CreateGameService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
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

        try {

            CreateGameService createGame = new CreateGameService();

            CreateGameResponse resp = createGame.create(req, game, auth);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (MissingDataException e) {

            response.status(400);

            return new ErrorFormatter(e).getErrorFormat();

        } catch (InvalidCredentialsException e) {

            response.status(401);

            return new ErrorFormatter(e).getErrorFormat();

        } catch (DataAccessException e) {

            response.status(500);

            return new ErrorFormatter(e).getErrorFormat();

        }
    }
}
