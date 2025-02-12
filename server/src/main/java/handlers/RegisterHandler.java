package handlers;

import request.RegisterRequest;
import spark.Request;
import spark.Response;

public class RegisterHandler extends BaseHandler {
    @Override
    public Object handle(Request request, Response response) throws Exception {

        RegisterRequest req = gson.fromJson(request.body(), RegisterRequest.class);
        return null;
    }
}
