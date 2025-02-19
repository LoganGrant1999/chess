package dataaccess;

import model.GameData;
import model.ListGameData;

import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO {

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<ListGameData> listGames(String authToken) throws DataAccessException;

    GameData joinGame(int gameID, String username, String playerColor, String gameName) throws DataAccessException;

    void clear();
}
