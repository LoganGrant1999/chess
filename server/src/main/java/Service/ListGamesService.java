package Service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import model.GameData;
import request.ListGamesRequest;
import response.ListGamesResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ListGamesService {

    public ListGamesResponse lister(ListGamesRequest req, MemoryAuthDAO auth, MemoryGameDAO game) throws DataAccessException {

        AuthData authData = auth.getAuth(req.authToken());

        if (authData == null || !Objects.equals(req.authToken(), authData.authToken())){

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        try{

            ArrayList<GameData> games = game.listGames(authData.authToken());

            return new ListGamesResponse(games);

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }
}
