package service;

import dataaccess.*;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import model.ListGameData;
import response.ListGamesResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ListGamesService {

    /* this method attempts to return a ListGamesResponse object containing ListGameData for each game
    in the GameDAO database. If successful, it returns this ListGamesResponse object. If the AuthData
     provided is null or the authToken provided isn't in the AuthDAO database, it throws a new
     InvalidCredentialsException. If it catches a DataAccessException, it throws a new one*/

    public ListGamesResponse listGames(String authToken, AuthDAO auth, GameDAO game) throws DataAccessException{

        try {

            AuthData authData = auth.getAuth(authToken);


            if (authData == null || !Objects.equals(authToken, authData.authToken())) {

                throw new InvalidCredentialsException("Error: unauthorized");
            }

            try{

                ArrayList<ListGameData> games = game.listGames(authData.authToken());

                return new ListGamesResponse(games);

            } catch (DataAccessException e){

                throw new DataAccessException(e.getMessage());
            }

        } catch (SQLException e) {

            throw new DataAccessException(e.getMessage());
        }
    }
}
