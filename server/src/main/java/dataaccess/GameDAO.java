package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames(String authToken) throws DataAccessException;

    GameData updateGame(int gameID, String username, String playerColor, String gameName) throws DataAccessException;

    void clear();
}
