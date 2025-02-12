package Service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.Authtoken;
import model.User;
import request.RegisterRequest;
import response.RegisterResponse;

public class RegisterService {

    private MemoryUserDAO dao = new MemoryUserDAO();

    public RegisterResponse register(RegisterRequest req){

        User user = new User(req.getUsername(), req.getPassword(), req.getEmail());

        if (dao.getUser(user.getUsername()) != null) {
            // Return failure response
            return new RegisterResponse("", "", 400, "User exists already");
        }

        // Insert the user
        Authtoken authtoken = dao.insertUser(user);

        // Successful Response
        return new RegisterResponse(user.getUsername(), authtoken.getAuthtoken(), 200, "Success");


//        return RegisterResponse();

    }


}
