package dataaccess;

import model.Authtoken;
import model.UserData;

public interface UserDAO{

    Authtoken insertUser(UserData userData);

    UserData getUser(String userName);

}
