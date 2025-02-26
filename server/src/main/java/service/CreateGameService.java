package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import exceptions.MissingDataException;
import request.CreateGameRequest;
import response.CreateGameResponse;

public class CreateGameService {

    /*Method attempts to create a new game and store it in the MemoryGameDAO map. If successful
    it returns a  CreateGameResponse. If the gameName from the request is null, it throws a new
    MissingDataException. If a DataAccessException is caught, it throws a new DataAccessException*/

    public CreateGameResponse createGame(CreateGameRequest req, MemoryGameDAO game) throws DataAccessException {

        if (req.gameName() == null){

            throw new MissingDataException("Error: bad request");
        }

        try{

            int newGameID = game.createGame(req.gameName());

            return new CreateGameResponse(newGameID);

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }
}
