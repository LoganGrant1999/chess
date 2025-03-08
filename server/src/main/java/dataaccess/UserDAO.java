package dataaccess;


import model.AuthData;
import model.UserData;

/* Interface used to create a data storage class that stores User data, either in storage or in a database.
 * Classes that import this interface inherit a createUser method used to store new user data,
 * a getUser method that can be used to retrieve UserData from where it is being stored,  and a clear method
 * used to wipe all data from where it is being stored */

public interface UserDAO{

    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String userName)throws DataAccessException ;

    void clear() throws Exception;

}
