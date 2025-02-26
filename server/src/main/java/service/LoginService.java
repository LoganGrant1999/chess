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

    /*This method attempts to return a LoginResponse object given a valid username and password that
    exist in the MemoryUserDAO map. If successful, it returns this LoginResponse object. If the username
    provided is null or the password doesn't match what's stored in MemoryUserDAO, it throws a new
    InvalidCredentialsException. If it catches a DataAccessException, it throws a new one.
     */

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
