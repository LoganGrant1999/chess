package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import response.LogoutResponse;

public class LogoutService {

    /*
    This method attempts to remove the AuthData for a given authToken from the MemoryAuthDAO map.
    If successful, it returns a new LogoutResponse object. If it catches a DataAccessException,
    it throws a new one.
     */
    public LogoutResponse logout(String authToken, MemoryAuthDAO auth) throws DataAccessException {

        try {

            auth.remove(authToken);

            return new LogoutResponse();

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }
}
