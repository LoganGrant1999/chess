package service;

import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;
import java.util.UUID;


public class RegisterService {

    /* This method attempts to return a new RegisterResponse object and to enter a new user's UserData
    * in the UserDAO database. If successful, it returns a new RegisterResponse object. If any of the
    * UserData fields provided are null, it throws a new MissingDataException. If the entered username
    * already exists in the UserDAO database, it returns a new AlreadyTakenException. If it catches a
    * DataAccessException, it throws a new one*/

    public RegisterResponse register(RegisterRequest req, UserDAO user, AuthDAO auth)  throws Exception {

        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new MissingDataException("Error: bad request");
        }

        UserData userData = new UserData(req.username(), req.password(), req.email());


        if (user.getUser(userData.username())!= null) {

            throw new AlreadyTakenException("Error: already taken");
        }

        String userToken = UUID.randomUUID().toString();

        try {

            user.createUser(userData);

        } catch(DataAccessException e) {

            throw new DataAccessException(e.getMessage());
        }

        try{

        AuthData authData = new AuthData(userToken, req.username());

        auth.createAuth(authData);

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }

        return new RegisterResponse(req.username(), userToken);
    }

}
