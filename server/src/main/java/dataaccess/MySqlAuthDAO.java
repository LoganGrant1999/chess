package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

//Placeholder class for Phase4 when I'll interact with the database and store AuthData there
public class MySqlAuthDAO implements AuthDAO {

    private int executeUpdate(String statement, Object... params) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            try(var prepStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                for (var i = 0; i < params.length; i++){
                    var param = params[i];
                    if (param instanceof String p) prepStatement.setString(i + 1, p);
                    else if (param instanceof Integer p) prepStatement.setInt(i + 1, p);
                    else if (param == null) prepStatement.setNull(i + 1, NULL);
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
    public void createAuth(AuthData authdata) throws DataAccessException {


    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void remove(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
