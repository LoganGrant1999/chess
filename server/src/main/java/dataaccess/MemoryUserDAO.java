package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private Map<String, UserData> db;

    public MemoryUserDAO() {
        this.db = new HashMap<>();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException{
        if (db.containsKey(userData.username())){
            throw new DataAccessException("username already exists");
        }
        db.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        if (!db.containsKey(username)){
            return null;
        }
        return db.get(username);
    }


    @Override
    public void clear() throws DataAccessException{
        if (db.isEmpty()){
            throw new DataAccessException("database is empty");
        }
        db.clear();
    }
}

