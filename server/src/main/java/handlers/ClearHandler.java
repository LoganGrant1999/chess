package handlers;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import response.ClearResponse;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends BaseHandler{

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

        try {

            ClearService clearService = new ClearService();

            ClearResponse resp = clearService.clearAllData(auth, game, user);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (DataAccessException e){

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
