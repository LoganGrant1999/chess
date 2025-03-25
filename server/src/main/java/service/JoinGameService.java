package service;

import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;
import response.JoinGameResponse;

public class JoinGameService {

    /*This method attempts to enable a user to join an existing game in the GameDAO database. If successful
     it returns a JoinGameResponse object. If any of the GameData passed into the method is null or not valid,
      it throws a new MissingDataException. If an AlreadyTakenException is caught, it throws a new one. If
      a DataAccessException is caught, it throws a new one*/

    public JoinGameResponse joinGame(JoinGameRequest req, UserDAO user, AuthDAO auth,
                                     GameDAO game, String authToken) throws Exception {

        if (req.playerColor() == null || req.gameID() == 0 || !(req.playerColor().equals("WHITE") || req.playerColor().equals("BLACK"))) {

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