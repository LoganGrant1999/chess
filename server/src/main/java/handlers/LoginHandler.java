package handlers;

import Service.LoginService;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.InvalidCredentialsException;
import exceptions.ServerException;
import request.LoginRequest;
import response.LoginResponse;
import spark.Request;
import spark.Response;

public class LoginHandler extends BaseHandler{

    private MemoryUserDAO user;

    private MemoryAuthDAO auth;

    public LoginHandler() {
        this.user = new MemoryUserDAO();
        this.auth = new MemoryAuthDAO();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        LoginRequest req = gson.fromJson(request.body(), LoginRequest.class);

        try{

            LoginService logService = new LoginService(user, auth);

            LoginResponse resp = logService.login(req);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (InvalidCredentialsException e){

            response.status(401);

            return new ErrorFormatter(e).getErrorFormat();

        }  catch (ServerException e) {

            response.status(500);

            return new ErrorFormatter(e).getErrorFormat();

        }

    }
}
