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
    public void createAuth(AuthData authdata) {
        db.put(authdata.authToken(), authdata);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return db.get(authToken);
    }

    @Override
    public void clear() {
        db.clear();
    }
}
