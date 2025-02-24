package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import response.ClearResponse;

public class ClearService {


    public ClearResponse clearAllData(MemoryAuthDAO auth, MemoryGameDAO game, MemoryUserDAO user) throws Exception{

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
