package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

/* Class creates a Map that stores Authentication data and implements methods from AuthDAO
interface for interacting with the data stored in the map
 */

public class MemoryAuthDAO implements AuthDAO {

    //initializes map where AuthData will be stored
    private Map<String, AuthData> db;

    public MemoryAuthDAO() {
        this.db = new HashMap<>();
    }

    //method creates entry into map for storing AuthData when a user registers or logs in
    @Override
    public void createAuth(AuthData authdata) throws DataAccessException{
        if (db.containsKey(authdata.authToken())){
            throw new DataAccessException("Error: AuthToken already created");
        }
        db.put(authdata.authToken(), authdata);
    }

    // method retrieves a user's AuthData from the map when given their current authToken
    @Override
    public AuthData getAuth(String authToken){
        if (!db.containsKey(authToken)){
            return null;
        }
        return db.get(authToken);
    }

    //method removes a user's AuthData from the map, typically when the user logs out
    @Override
    public void remove(String authToken) throws DataAccessException {
        if (!db.containsKey(authToken)){
            throw new DataAccessException("Error: authToken does not exist");
        }
        db.remove(authToken);
    }

    //method clears all AuthData from the map
    @Override
    public void clear() throws DataAccessException{
        try {
            db.clear();
        } catch (Exception e) {

            throw new DataAccessException("Error: couldn't clear the database");
        }
    }
}
