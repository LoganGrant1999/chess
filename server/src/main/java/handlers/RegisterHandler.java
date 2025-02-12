package handlers;

import Service.RegisterService;
import request.RegisterRequest;
import spark.Request;
import spark.Response;

public class RegisterHandler extends BaseHandler {

    private RegisterService service = new RegisterService();

    @Override
    public Object handle(Request request, Response response) throws Exception {

        RegisterRequest req = gson.fromJson(request.body(), RegisterRequest.class);

        return null;
    }
}
