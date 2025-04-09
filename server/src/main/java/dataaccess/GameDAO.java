package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.ListGameData;

import java.util.ArrayList;
import java.util.Collection;


/* Interface used to create a data storage class that stores Game data, either in storage or in a database.
 * Classes that import this interface inherit a createGame method used to store new game data,
 * a getGame method that can be used to retrieve GameData from where it is being stored, a
 * listGames method that can be used to return a list of all games stored in the database, a joinGame
 * method that enables a user to join an existing game, and a clear method used to wipe all data from where it
 * is being stored */

public interface GameDAO {

    public void updateGame(int gameID, GameData gameData) throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<ListGameData> listGames(String authToken) throws DataAccessException;

    GameData joinGame(int gameID, String username, String playerColor, String gameName) throws DataAccessException;

    void clear() throws DataAccessException;


}
