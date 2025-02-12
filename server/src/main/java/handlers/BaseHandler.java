package handlers;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public abstract class BaseHandler implements Route {
    final Gson gson = new Gson();
}
