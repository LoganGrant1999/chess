package Service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import request.LoginRequest;
import response.LoginResponse;

public class LoginService {

    private UserDAO user;
    private AuthDAO auth;

    public LoginService(UserDAO user, AuthDAO auth) {
        this.user = user;
        this.auth = auth;
    }


    public LoginResponse login(LoginRequest req){

        return null;
    }
}
