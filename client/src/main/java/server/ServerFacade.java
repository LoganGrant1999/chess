package server;

import com.google.gson.Gson;
import exceptions.NetworkException;
import request.LoginRequest;
import request.RegisterRequest;
import response.ClearResponse;
import response.LoginResponse;
import response.RegisterResponse;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public RegisterResponse register(RegisterRequest req) throws NetworkException {

        var path = "/user";

        return this.makeRequest("POST", path, req, RegisterResponse.class, null);
    }

    public LoginResponse login(LoginRequest req) throws NetworkException {

        var path = "/session";

        return this.makeRequest("POST", path, req, LoginResponse.class, null);
    }

    public ClearResponse clear() throws NetworkException {

        var path = "/db";

        return this.makeRequest("DELETE", path, null, ClearResponse.class, null);

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws NetworkException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);

            if (authToken != null){
                http.addRequestProperty("Authorization", authToken);
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (NetworkException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new NetworkException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, NetworkException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw NetworkException.fromJson(respErr);
                }
            }
            throw new NetworkException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}

