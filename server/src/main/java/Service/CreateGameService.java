package Service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.InvalidCredentialsException;
import exceptions.MissingDataException;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import response.CreateGameResponse;

import java.util.Objects;

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
