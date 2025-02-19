package Service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import request.LogoutRequest;
import response.LogoutResponse;

import java.util.Objects;

public class LogoutService {

    public LogoutResponse logout(LogoutRequest req, String authToken, MemoryAuthDAO auth) throws DataAccessException {

        try {

            auth.remove(authToken);

            return new LogoutResponse();

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }
}
