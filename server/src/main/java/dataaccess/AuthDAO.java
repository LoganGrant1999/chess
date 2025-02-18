package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void createAuth(AuthData authdata) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;

}
