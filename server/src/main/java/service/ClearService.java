package service;

import dataaccess.*;
import response.ClearResponse;

public class ClearService {

    /* method trys to clear all data from each of the DAO databases/maps. If a DataAccessException is caught
    * it throws a new DataAccessException*/

    public ClearResponse clearAllData(AuthDAO auth, GameDAO game, UserDAO user) throws Exception{

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
