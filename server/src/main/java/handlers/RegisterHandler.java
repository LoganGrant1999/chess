package handlers;

import Service.RegisterService;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import exceptions.ServerException;
import request.RegisterRequest;
import response.RegisterResponse;
import spark.Request;
import spark.Response;


import java.util.Map;

public class RegisterHandler extends BaseHandler{


    private MemoryUserDAO user;

    private MemoryAuthDAO auth;


    public RegisterHandler(MemoryUserDAO user, MemoryAuthDAO auth) {
        this.user = user;
        this.auth = auth;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        RegisterRequest req = gson.fromJson(request.body(), RegisterRequest.class);

        try{

            RegisterService regService = new RegisterService();

            RegisterResponse resp = regService.register(req, user, auth);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (MissingDataException e){

            response.status(400);

            return new ErrorFormatter(e).getErrorFormat();

        } catch (AlreadyTakenException e){

            response.status(403);

            return new ErrorFormatter(e).getErrorFormat();

        } catch (ServerException e){

            response.status(500);

            return new ErrorFormatter(e).getErrorFormat();

        }
    }
}