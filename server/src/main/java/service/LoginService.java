package service;

import dataaccess.*;
import exceptions.InvalidCredentialsException;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import response.LoginResponse;

import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public LoginResponse login(LoginRequest req, MemoryUserDAO user, MemoryAuthDAO auth) throws Exception {

        UserData userData = user.getUser(req.username());

        if (userData == null || !Objects.equals(req.password(), userData.password())){

            throw new InvalidCredentialsException("Error: unauthorized");

        }

        String userToken = UUID.randomUUID().toString();

        try{

            AuthData authData = new AuthData(userToken, req.username());

            auth.createAuth(authData);

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }

        return new LoginResponse(req.username(), userToken);
    }
}
