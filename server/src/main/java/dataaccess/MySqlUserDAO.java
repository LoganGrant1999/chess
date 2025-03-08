package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Statement;


//Placeholder class for Phase4 when I'll interact with the database and store UserData there
public class MySqlUserDAO implements UserDAO {


    private int executeUpdate(String statement, Object... params) throws DataAccessException {

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

        } catch (DataAccessException e) {

            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, userName);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"),
                                rs.getString("password"), rs.getString("email"));
                    }
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
            executeUpdate(statement);
    }
}
