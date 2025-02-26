package handlers;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;


/* Abstract class that enables all handler classes to extend
this and more easily convert between gson and json
 */

public abstract class BaseHandler implements Route {

    final Gson gson = new Gson();
}
