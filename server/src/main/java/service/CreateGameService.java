package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import exceptions.MissingDataException;
import request.CreateGameRequest;
import response.CreateGameResponse;

public class CreateGameService {

    public CreateGameResponse create(CreateGameRequest req, MemoryGameDAO game) throws DataAccessException {

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
