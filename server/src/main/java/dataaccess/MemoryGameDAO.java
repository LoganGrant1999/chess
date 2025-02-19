package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.ListGameData;
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
    public ArrayList<ListGameData> listGames(String authToken) throws DataAccessException {
        if (db.isEmpty()){
            return new ArrayList<>();
        }

        try {
            ArrayList<ListGameData> listedGames = new ArrayList<>();



            for (GameData games: db.values()){

                String whiteUsername;
                String blackUsername;

                if (games.whiteUsername() == null){

                    whiteUsername = "";

                } else{

                    whiteUsername = games.whiteUsername();
                }

                if (games.blackUsername() == null){

                    blackUsername = "";
                } else {

                    blackUsername = games.blackUsername();
                }

                ListGameData gameData = new ListGameData(games.gameID(), whiteUsername, blackUsername, games.gameName());
                listedGames.add(gameData);
            }

            return listedGames;

        } catch (Exception e){
            throw new DataAccessException("Error: listGames did not return ArrayList of GameData");
        }
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
