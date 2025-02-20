package dataaccess;


import model.AuthData;
import model.UserData;

public interface UserDAO{

    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String userName)throws DataAccessException ;

    void clear() throws DataAccessException;

}
