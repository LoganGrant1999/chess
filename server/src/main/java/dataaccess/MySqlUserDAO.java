package dataaccess;

import model.UserData;

import java.sql.SQLException;

//Placeholder class for Phase4 when I'll interact with the database and store UserData there
public class MySqlUserDAO implements UserDAO{


    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }

}
