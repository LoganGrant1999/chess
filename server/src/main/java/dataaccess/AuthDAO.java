package dataaccess;

import model.AuthData;

import java.sql.SQLException;

/* Interface used to create a data storage class that stores Authentication data, either in storage or in a database.
* Classes that import this interface inherit a createAuth method used to store new authentication data,
* a getAuth method that can be used to retrieve AuthData from where it is being stored, a remove method that
* is used to take a single AuthData entry out of storage, and a clear method used to wipe all data from where it
* is being stored */

public interface AuthDAO {

    void createAuth(AuthData authdata) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException, SQLException;

    void remove(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;

}
