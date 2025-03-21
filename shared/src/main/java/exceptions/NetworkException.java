package exceptions;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class NetworkException extends Exception {
    final private int statusCode;

    public NetworkException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson() {

        return new Gson().toJson(Map.of("message", getMessage(), "status", statusCode));
    }

    public static NetworkException fromJson(InputStream stream) {
        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
        Object status = map.get("status");
        int stat = (status instanceof Double d) ? d.intValue() : 500;
        String msg = map.get("message").toString();
        return new NetworkException(stat, msg);
    }

    public int StatusCode() {
        return statusCode;
    }
}