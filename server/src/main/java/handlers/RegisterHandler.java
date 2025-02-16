package handlers;

import Service.RegisterService;
import request.RegisterRequest;
import response.RegisterResponse;
import spark.Request;
import spark.Response;

import java.util.Map;

public class RegisterHandler extends BaseHandler{



    @Override
    public Object handle(Request request, Response response) throws Exception {

        RegisterRequest req = gson.fromJson(request.body(), RegisterRequest.class);

        try{

            RegisterResponse resp = RegisterService.register(req);

            String jsonResp = gson.toJson(resp);

            response.status(200);

            return jsonResp;

        } catch (BadRequestException e){

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