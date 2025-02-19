package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private Map<String, AuthData> db;

    public MemoryAuthDAO() {
        this.db = new HashMap<>();
    }

    @Override
    public void createAuth(AuthData authdata) throws DataAccessException{
        if (db.containsKey(authdata.authToken())){
            throw new DataAccessException("AuthToken already created");
        }
        db.put(authdata.authToken(), authdata);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (!db.containsKey(authToken)){
            return null;
        }
        return db.get(authToken);
    }

    @Override
    public void clear() throws DataAccessException{
        if (db.isEmpty()){
            throw new DataAccessException("database is empty");
        }
        db.clear();
    }
}
