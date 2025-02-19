package Service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import request.LogoutRequest;
import response.LogoutResponse;

import java.util.Objects;

public class LogoutService {

    public LogoutResponse logout(LogoutRequest req, MemoryAuthDAO auth) throws DataAccessException {

        AuthData authData = auth.getAuth(req.authToken());

        if (authData == null || !Objects.equals(req.authToken(), authData.authToken())){

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        try {

            auth.remove(authData.authToken());

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }

        LogoutResponse logResp = new LogoutResponse();

        return logResp;
    }
}
