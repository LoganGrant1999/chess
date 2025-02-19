package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.*;

public class MemoryGameDAO implements GameDAO{

    private Map<Integer, GameData> db;

    private  int gameIDCounter = 0;

    public MemoryGameDAO() {
        this.db = new HashMap<>();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException{

        if (db.values().stream().anyMatch(game -> game.gameName().equals(gameName))) {
            throw new DataAccessException("Error: game name already taken");
        }

        GameData newGame = new GameData(gameIDCounter, null, null, gameName, new ChessGame());

        db.put(newGame.gameID(), newGame);

        gameIDCounter = gameIDCounter + 1;

        return newGame.gameID();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if (!db.containsKey(gameID)){
            return null;
        }
        return db.get(gameID);
    }

    @Override
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (db.isEmpty()){
            return Collections.emptyList();
        }
        return new ArrayList<>(db.values());
    }

    @Override
    public GameData updateGame(int gameID, String username, String playerColor, String gameName) throws DataAccessException {
        if (getGame(gameID) == null){
            throw new DataAccessException("Error: game does not exist");
        }

        GameData currGame = getGame(gameID);

        if (Objects.equals(playerColor, "Black")) {
            GameData updatedGame =  new GameData(gameID, currGame.whiteUsername(), username, gameName, currGame.game());
            db.put(updatedGame.gameID(), updatedGame);
            return updatedGame;
        }
        else {
            GameData updatedGame = new GameData(gameID, username, currGame.blackUsername(), gameName, currGame.game());
            db.put(updatedGame.gameID(), updatedGame);
            return updatedGame;
        }
    }

    @Override
    public void clear() {
        db.clear();
    }
}
