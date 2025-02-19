package handlers;

import Service.ListGamesService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.InvalidCredentialsException;
import request.ListGamesRequest;
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

        ListGamesRequest req = gson.fromJson(request.body(), ListGamesRequest.class);

        try{

            ListGamesService listService = new ListGamesService();

            ListGamesResponse resp = listService.lister(req, auth, game);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (InvalidCredentialsException e) {

            response.status(401);

            return new ErrorFormatter(e).getErrorFormat();

        } catch (DataAccessException e){

            response.status(500);

            return new ErrorFormatter(e).getErrorFormat();
        }
    }
}
