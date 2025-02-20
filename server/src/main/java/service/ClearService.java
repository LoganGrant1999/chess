package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import response.ClearResponse;
import spark.Response;

public class ClearService {


    public ClearResponse clearer(MemoryAuthDAO auth, MemoryGameDAO game, MemoryUserDAO user) throws Exception{

        try {

            auth.clear();
            game.clear();
            user.clear();

            return new ClearResponse();

        } catch (DataAccessException e) {

            throw new DataAccessException(e.getMessage());
        }

    }
}
