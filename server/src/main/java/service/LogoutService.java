package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import exceptions.InvalidCredentialsException;
import response.LogoutResponse;

import java.sql.SQLException;

public class LogoutService {

    /*
    This method attempts to remove the AuthData for a given authToken from the AuthDAO database.
    If successful, it returns a new LogoutResponse object. If the authToken provided isn't in the
     map, it throws a new InvalidCredentialsException. If it catches a DataAccessException,
    it throws a new one.
     */
    public LogoutResponse logout(String authToken, AuthDAO auth) throws DataAccessException {

        /*
        checks to see if authToken is null or absent from map storage. If so, sets status at 401 and returns json
        of InvalidCredentialsException that's thrown along with its message
         */
        try {

            if (authToken == null || auth.getAuth(authToken) == null) {

                throw new InvalidCredentialsException("Error: not authorized");
            }

            try {

                auth.remove(authToken);

                return new LogoutResponse();

            } catch (DataAccessException e) {

                throw new DataAccessException(e.getMessage());
            }

        } catch (SQLException e) {

            throw new DataAccessException(e.getMessage());
        }
    }
}
