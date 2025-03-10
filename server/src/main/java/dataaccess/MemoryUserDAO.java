package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

/* Class creates a Map that stores Authentication data and implements methods from AuthDAO
interface for interacting with the data stored in the map
 */

public class MemoryUserDAO implements UserDAO{

    //initializes map where UserData will be stored
    private Map<String, UserData> db;

    public MemoryUserDAO() {
        this.db = new HashMap<>();
    }

    //Method used to add new UserData to the map, usually when a new user registers
    @Override
    public void createUser(UserData userData) throws DataAccessException{
        if (db.containsKey(userData.username())){
            throw new DataAccessException("Error: username already exists");
        }
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());

        UserData userData1 = new UserData(userData.username(), hashedPassword, userData.email());

        db.put(userData1.username(), userData1);
    }

    //Method used to retrieve the UserData of a user in the map given the User's username
    @Override
    public UserData getUser(String username) throws DataAccessException{
        if (!db.containsKey(username)){
            return null;
        }
        return db.get(username);
    }

    //Method used to clear all data stored in the map
    @Override
    public void clear() throws DataAccessException{
        try{
            db.clear();
        } catch (Exception e) {

            throw new DataAccessException("Error: couldn't clear the database");
        }
    }
}

