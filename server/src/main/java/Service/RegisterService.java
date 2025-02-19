package Service;

import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;
import java.util.UUID;


public class RegisterService {

    public RegisterResponse register(RegisterRequest req, MemoryUserDAO user, MemoryAuthDAO auth) throws DataAccessException {

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
