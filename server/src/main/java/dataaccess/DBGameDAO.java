package dataaccess;

import model.GameData;
import model.ListGameData;

import java.util.ArrayList;

//Placeholder class for Phase4 when I'll interact with the database and store GameData there
public class DBGameDAO implements GameDAO {
    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<ListGameData> listGames(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public GameData joinGame(int gameID, String username, String playerColor, String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
