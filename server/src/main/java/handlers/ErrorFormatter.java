package handlers;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

/* Class takes in a thrown exception, gets its message, and converts it to json
 with the desired format using a map converted to a string by gson
 */

public class ErrorFormatter {

    //instantiates the map and errormessage string
    private Map<String, String> errorFormat;
    private String errorMessage;

    public ErrorFormatter(Exception e) {

        this.errorFormat = new HashMap<>();

        this.errorMessage = e.getMessage();

        String message = "message";

        errorFormat.put(message, errorMessage);
    }

    //method takes map from constructor and converts it Json format
    public String getErrorFormat() {

        Gson gson = new Gson();

        return gson.toJson(errorFormat);
    }
}