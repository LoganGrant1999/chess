package dataaccess;

import model.Authtoken;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private Map<String, User> db;

    public MemoryUserDAO() {
        this.db = new HashMap<>();
    }

    @Override
    public Authtoken insertUser(User user) {
          db.put(user.getUsername(), user);

          return new Authtoken();
    }

    @Override
    public User getUser(String userName) {
        return db.get(userName);
    }
}
