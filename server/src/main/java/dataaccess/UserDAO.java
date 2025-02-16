package dataaccess;


import model.AuthData;
import model.UserData;

public interface UserDAO{

    void createUser(UserData userData);

    UserData getUser(String userName);

    void clear();

}
