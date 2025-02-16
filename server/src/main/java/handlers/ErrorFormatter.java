package handlers;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class ErrorFormatter{


    private Map<String, String> errorFormat;
    private String errorMessage;


    public ErrorFormatter(Exception e) {
        this.errorFormat = new HashMap<>();
        this.errorMessage = e.getMessage();
        String message = "message";
        errorFormat.put(message, errorMessage);

    }

    public String getErrorFormat() {
        final Gson gson = new Gson();
        String jsonError = gson.toJson(errorFormat);
        return jsonError;
    }
}
