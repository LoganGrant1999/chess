package dataaccess;

import exceptions.MissingDataException;
import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;


// DAO class that interacts with auth table in database
public class MySqlAuthDAO implements AuthDAO {

    // Method that takes in a SQL statement and parameters and executes updates within the auth table directly
    private int executeUpdate(String statement, Object... params) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {

            try (var prepStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {

                for (var i = 0; i < params.length; i++){

                    var param = params[i];

                    if (param instanceof String p) {

                        prepStatement.setString(i + 1, p);
                    }

                }

                prepStatement.executeUpdate();

                var keys = prepStatement.getGeneratedKeys();

                if (keys.next()) {

                    return keys.getInt(1);
                }

                return 0;
            }

        } catch (SQLException e){

            throw new DataAccessException(e.getMessage());
        }
    }

    // Method designed to create a new record of auth data in the auth table
    @Override
    public void createAuth(AuthData authdata) throws DataAccessException {

        try {

            var statement = "INSERT INTO auth (authToken, username) values (?, ?)";

            executeUpdate(statement, authdata.authToken(), authdata.username());

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }

    // Method designed to retrieve AuthData from the auth table given a valid authToken
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {

        if (authToken == null){

            throw new MissingDataException("Error: Bad Request");
        }

        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";

            try (var ps = conn.prepareStatement(statement)){

                ps.setString(1, authToken);

                try (var rs = ps.executeQuery()){

                    if (rs.next()) {

                        return new AuthData(rs.getString("authToken"),
                                rs.getString("username"));
                    }
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(e.getMessage());
        }

        return null;
    }

    //Method designed to remove an existing record of AuthData from the database given a valid authToken
    @Override
    public void remove(String authToken) throws DataAccessException {

        try {

            var statement = "DELETE FROM auth WHERE authToken=?";

            executeUpdate(statement, authToken);

        } catch (DataAccessException e){

            throw new DataAccessException(e.getMessage());
        }
    }

    //Method designed to truncate auth table from chess database
    @Override
    public void clear() throws DataAccessException {

        var statement = "TRUNCATE auth";

        executeUpdate(statement);
    }
}
