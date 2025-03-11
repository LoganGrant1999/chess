package dataaccess;

import jdk.dynalink.beans.StaticClass;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    //SQL code that configureDatabase uses to create tables if they don't exist at startup
    private static final String[] CREATESTATEMENTS = {

            "USE " + DATABASE_NAME + ";"
            ,

            """
            CREATE TABLE IF NOT EXISTS user(
            	username varchar(50) NOT NULL,
                password varchar(60) NOT NULL,
                email varchar(50) NOT NULL,
                PRIMARY KEY(username)
            );
            """,

            """
            
            CREATE TABLE IF NOT EXISTS auth(
            	authToken varchar(36) NOT NULL,
                username varchar(50) NOT NULL,
                PRIMARY KEY(authToken)
            );
            """,

            """
           CREATE TABLE IF NOT EXISTS game(
                gameID int NOT NULL AUTO_INCREMENT,
                whiteUsername varchar(50),
                blackUsername varchar(50),
                gameName varchar(50) NOT NULL,
                chessGame longtext NOT NULL,
                PRIMARY KEY(gameID)
            );
           """
    };

    /* Method that configures the database and tables upon server start up
     ensuring they exist or throwing a DataAccessException
     */
    public static void configureDatabase() throws Exception{
        createDatabase();
        try (var conn = getConnection()){
            for (var statement: CREATESTATEMENTS) {
                try(var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
