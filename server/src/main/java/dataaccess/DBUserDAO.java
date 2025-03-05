package dataaccess;

import model.UserData;

import java.sql.SQLException;

//Placeholder class for Phase4 when I'll interact with the database and store UserData there
public class DBUserDAO implements UserDAO{
    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }




    //Array of SQL statements used to create user table if it doesn't exist in the database
    private final String[] createStatments= {
            """
            CREATE TABLE user(
            	username varchar(50) NOT NULL,
                password varchar(60) NOT NULL,
                email varchar(50) NOT NULL,
                PRIMARY KEY(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDB() throws DataAccessException{

        DatabaseManager.createDatabase();

        try (var conn = DatabaseManager.getConnection()){

            for(var statement: createStatments) {

                try(var preparedStatement = conn.prepareStatement(statement)){

                    preparedStatement.update();
                }

            }

        } catch (SQLException e){

            throw new DataAccessException(e.getMessage());
        }
    }

}
