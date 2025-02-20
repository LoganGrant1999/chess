package handlers;

import service.LogoutService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.InvalidCredentialsException;
import response.LogoutResponse;
import spark.Request;
import spark.Response;

public class LogoutHandler extends BaseHandler{

    private MemoryAuthDAO auth;

    public LogoutHandler(MemoryAuthDAO auth) {
        this.auth = auth;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String authToken = request.headers("Authorization");

        if (authToken == null || auth.getAuth(authToken) == null) {

            response.status(401);

            String jsonResp = new ErrorFormatter(new InvalidCredentialsException("Error: unauthorized")).getErrorFormat();

            return jsonResp;
        }

        try{

            LogoutService logService = new LogoutService();

            LogoutResponse resp = logService.logout(authToken, auth);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}
