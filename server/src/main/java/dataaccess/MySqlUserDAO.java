package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.foreign.MemorySegment.NULL;

//Placeholder class for Phase4 when I'll interact with the database and store UserData there
public class MySqlUserDAO implements UserDAO {


    private int executeUpdate(String statement, Object... params) throws Exception{
        try(var conn = DatabaseManager.getConnection()){
            try(var prepStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                for (var i = 0; i < params.length; i++){
                    var param = params[i];
                    if (param instanceof String p) prepStatement.setString(i + 1, p);
                }
                prepStatement.executeUpdate();

                var keys = prepStatement.getGeneratedKeys();
                if (keys.next()){
                    return keys.getInt(1);
                }

                return 0;
            }

        } catch (SQLException e){

            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {

        try {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            executeUpdate(statement, userData.username(), userData.password(), userData.email());

        } catch (Exception e) {

            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }

}
