package handlers;

import Service.LogoutService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.InvalidCredentialsException;
import request.LogoutRequest;
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

        LogoutRequest req = gson.fromJson(request.body(), LogoutRequest.class);

        String authToken = request.headers("Authorization");

        if (authToken == null || auth.getAuth(authToken) == null) {
            throw new InvalidCredentialsException("Error: unauthorized");
        }

        try{

            LogoutService logService = new LogoutService();

            LogoutResponse resp = logService.logout(req, authToken, auth);

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
