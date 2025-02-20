package handlers;

import service.RegisterService;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import request.RegisterRequest;
import response.RegisterResponse;
import spark.Request;
import spark.Response;

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

            String jsonResp = new ErrorFormatter(new MissingDataException(e.getMessage())).getErrorFormat();

            return jsonResp;

        } catch (AlreadyTakenException e){

            response.status(403);

            String jsonResp = new ErrorFormatter(new AlreadyTakenException(e.getMessage())).getErrorFormat();

            return jsonResp;

        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}