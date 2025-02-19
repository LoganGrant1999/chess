package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import handlers.ErrorFormatter;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;
import response.JoinGameResponse;

public class JoinGameService {

    public JoinGameResponse join(JoinGameRequest req, MemoryUserDAO user, MemoryAuthDAO auth,
                                 MemoryGameDAO game, String authToken) throws Exception {

        if (req.gameID() == 0 || !(req.playerColor().equals("WHITE") || req.playerColor().equals("BLACK"))) {

            throw new MissingDataException("Error: bad request");
        }

        AuthData authData = auth.getAuth(authToken);

        UserData userData = user.getUser(authData.username());

        GameData gameData = game.getGame(req.gameID());

        try{

            game.joinGame(req.gameID(), userData.username(), req.playerColor(), gameData.gameName());

            return new JoinGameResponse();

        } catch (AlreadyTakenException e){

            throw new AlreadyTakenException(e.getMessage());

        } catch (DataAccessException e) {

            throw new DataAccessException(e.getMessage());
        }
    }
}
