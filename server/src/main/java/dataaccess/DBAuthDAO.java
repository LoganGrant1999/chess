package dataaccess;

import model.AuthData;

//Placeholder class for Phase4 when I'll interact with the database and store AuthData there
public class DBAuthDAO implements AuthDAO {
    @Override
    public void createAuth(AuthData authdata) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void remove(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
