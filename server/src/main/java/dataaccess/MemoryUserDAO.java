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
    public void createUser(UserData userData) {
        db.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return db.get(username);
    }


    @Override
    public void clear() {
        db.clear();
    }
}

