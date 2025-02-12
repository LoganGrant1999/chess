package dataaccess;

import model.Authtoken;
import model.User;

public interface UserDAO{

    Authtoken insertUser(User user);

    User getUser(String userName);

}
