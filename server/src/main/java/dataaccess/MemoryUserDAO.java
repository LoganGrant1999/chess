package dataaccess;

import model.Authtoken;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private Map<String, UserData> db;

    public MemoryUserDAO() {
        this.db = new HashMap<>();
    }

    @Override
    public Authtoken insertUser(UserData userData) {
          db.put(userData.getUsername(), userData);

          return new Authtoken();
    }

    @Override
    public UserData getUser(String userName) {
        return db.get(userName);
    }
}
