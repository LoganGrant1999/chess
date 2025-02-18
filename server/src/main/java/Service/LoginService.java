package Service;

import dataaccess.AuthDAO;
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

    private UserDAO user;
    private AuthDAO auth;

    public LoginService(UserDAO user, AuthDAO auth) {
        this.user = user;
        this.auth = auth;
    }

    public LoginResponse login(LoginRequest req){

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
