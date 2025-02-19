package Service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import model.GameData;
import model.ListGameData;
import response.ListGamesResponse;

import java.util.ArrayList;
import java.util.Objects;

public class ListGamesService {

    public ListGamesResponse lister(String authToken, MemoryAuthDAO auth, MemoryGameDAO game) throws DataAccessException {

        AuthData authData = auth.getAuth(authToken);

        if (authData == null || !Objects.equals(authToken, authData.authToken())){

            throw new InvalidCredentialsException("Error: unauthorized");
        }

        try{

            ArrayList<ListGameData> games = game.listGames(authData.authToken());

            return new ListGamesResponse(games);

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }
}
