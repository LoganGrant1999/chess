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

    /*initializes MemoryAuthDAO and MemoryGameDAO objects to make calling their
    class methods easier
    */
    private MemoryUserDAO user;

    private MemoryAuthDAO auth;


    public RegisterHandler(MemoryUserDAO user, MemoryAuthDAO auth) {
        this.user = user;
        this.auth = auth;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        RegisterRequest req = gson.fromJson(request.body(), RegisterRequest.class);


        /*Attempts to call register from RegisterService. If successful, sets status to 200 and returns
        json of resulting RegisterResponse object.
         */
        try{

            RegisterService regService = new RegisterService();

            RegisterResponse resp = regService.register(req, user, auth);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

            /*if call to register results in MissingDataException, catches the exception, sets status at 400, and
            returns json of the MissingDataException and its message.
             */
        } catch (MissingDataException e){

            response.status(400);

            String jsonResp = new ErrorFormatter(new MissingDataException(e.getMessage())).getErrorFormat();

            return jsonResp;

              /*if call to register results in AlreadyTakenException, catches the exception, sets status to 403, and
            returns json of the AlreadyTakenException and its message
             */
        } catch (AlreadyTakenException e){

            response.status(403);

            String jsonResp = new ErrorFormatter(new AlreadyTakenException(e.getMessage())).getErrorFormat();

            return jsonResp;

            /*
            catches DataAccessException thrown by ListGamesService, sets status to 500, and returns json
            of Exception and message
             */
        } catch (DataAccessException e) {

            response.status(500);

            String jsonResp = new ErrorFormatter(new DataAccessException(e.getMessage())).getErrorFormat();

            return jsonResp;
        }
    }
}