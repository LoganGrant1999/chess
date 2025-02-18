package Service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import exceptions.InvalidCredentialsException;
import exceptions.ServerException;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import response.LoginResponse;

import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public LoginResponse login(LoginRequest req, MemoryUserDAO user, MemoryAuthDAO auth){

        UserData userData = user.getUser(req.username());

        if (userData == null || !Objects.equals(req.password(), userData.password())){

            throw new InvalidCredentialsException("username and/or password are incorrect");

        }

        String userToken = UUID.randomUUID().toString();

        try{

            AuthData authData = new AuthData(userToken, req.username());

            auth.createAuth(authData);

        } catch (RuntimeException e){

            throw new ServerException("Error Creating Auth");
        }

        return new LoginResponse(req.username(), userToken);
    }
}
