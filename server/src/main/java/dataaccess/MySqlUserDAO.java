package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;


//DAO class that interacts with user data in the database
public class MySqlUserDAO implements UserDAO {

    // Method that takes in a SQL statement and parameters and executes updates within the user table directly
    private int executeUpdate(String statement, Object... params) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()){

            try(var prepStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){

                for (var i = 0; i < params.length; i++){

                    var param = params[i];

                    if (param instanceof String p) prepStatement.setString(i + 1, p);

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

    //Method designed to create a new record of user data in the user table
    @Override
    public void createUser(UserData userData) throws DataAccessException {

        try {

            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

            String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());

            executeUpdate(statement, userData.username(), hashedPassword, userData.email());

        } catch (DataAccessException e) {

            throw new DataAccessException(e.getMessage());
        }
    }

    //Method designed to retrieve user data from the user table given a valid username
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

    //Method designed to truncate user table from chess database
    @Override
    public void clear() throws DataAccessException {

        var statement = "TRUNCATE user";

        executeUpdate(statement);
    }
}
