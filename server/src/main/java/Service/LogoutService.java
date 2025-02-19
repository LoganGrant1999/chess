package Service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import response.LogoutResponse;

public class LogoutService {

    public LogoutResponse logout(String authToken, MemoryAuthDAO auth) throws DataAccessException {

        try {

            auth.remove(authToken);

            return new LogoutResponse();

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }
}
