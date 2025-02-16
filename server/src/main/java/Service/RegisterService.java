package Service;

import dataaccess.MemoryUserDAO;
import model.Authtoken;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;

public class RegisterService {

    private MemoryUserDAO dao = new MemoryUserDAO();

    public static RegisterResponse register(RegisterRequest req){

        UserData userData = new UserData(req.getUsername(), req.getPassword(), req.getEmail());

        if (dao.getUser(UserData.getUsername()) != null) {
            // Return failure response
            return new RegisterResponse("", "", 400, "User exists already");
        }

        // Insert the user
        Authtoken authtoken = dao.insertUser(userData);

        // Successful Response
        return new RegisterResponse(userData.getUsername(), authtoken.getAuthtoken(), 200, "Success");


//        return RegisterResponse();

    }


}
