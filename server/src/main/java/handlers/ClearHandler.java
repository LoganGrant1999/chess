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

            clearService.clearer(auth, game, user);

            response.status(200);

            return new ClearResponse();

        } catch (DataAccessException e){

            response.status(500);

            return new ErrorFormatter(e).getErrorFormat();
        }
    }
}
