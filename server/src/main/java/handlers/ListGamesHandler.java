package handlers;

import service.ListGamesService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.InvalidCredentialsException;
import response.ListGamesResponse;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends BaseHandler{
    private MemoryAuthDAO auth;
    private MemoryGameDAO game;

    public ListGamesHandler(MemoryAuthDAO auth, MemoryGameDAO game) {
        this.auth = auth;
        this.game = game;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String authToken = request.headers("Authorization");

        if (authToken == null || auth.getAuth(authToken) == null) {

            response.status(401);

            return new InvalidCredentialsException("Error: unauthorized");
        }

        try{

            ListGamesService listService = new ListGamesService();

            ListGamesResponse resp = listService.lister(authToken, auth, game);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (DataAccessException e){

            response.status(500);

            return new ErrorFormatter(e).getErrorFormat();
        }
    }
}
