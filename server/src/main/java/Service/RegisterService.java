package Service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import exceptions.ServerException;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;
import java.util.UUID;


public class RegisterService {


    private UserDAO user;
    private AuthDAO auth;

    public RegisterService(UserDAO user, AuthDAO auth) {
        this.user = user;
        this.auth = auth;
    }

    public RegisterResponse register(RegisterRequest req) {

        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new MissingDataException("User Request is missing username, password, or email");
        }

        UserData userData = new UserData(req.username(), req.password(), req.email());


        if (user.getUser(userData.username()) != null) {

            throw new AlreadyTakenException("Username '" + req.username() + "' is already taken.");
        }


        String userToken = UUID.randomUUID().toString();

        try {

            user.createUser(userData);

        } catch(RuntimeException e) {

            throw new ServerException("Error Creating User");
        }


        try{

        AuthData authData = new AuthData(userToken, req.username());

        auth.createAuth(authData);

        } catch (RuntimeException e){

            throw new ServerException("Error Creating Auth");
        }

        return new RegisterResponse(req.username(), userToken);

    }

}
