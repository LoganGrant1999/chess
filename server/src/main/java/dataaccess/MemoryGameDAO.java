package dataaccess;

import chess.ChessGame;
import exceptions.AlreadyTakenException;
import model.GameData;
import model.ListGameData;
import model.UserData;

import java.util.*;

public class MemoryGameDAO implements GameDAO{

    private Map<Integer, GameData> db;

    private  int gameIDCounter = 1;

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


                ListGameData gameData = new ListGameData(games.gameID(), games.whiteUsername(), games.blackUsername(), games.gameName());
                listedGames.add(gameData);
            }

            return listedGames;

        } catch (Exception e){
            throw new DataAccessException("Error: listGames did not return ArrayList of GameData");
        }
    }

    @Override
    public GameData joinGame(int gameID, String username, String playerColor, String gameName) throws DataAccessException {

        if (getGame(gameID) == null){

            throw new DataAccessException("Error: game does not exist");

        }

        GameData currGame = getGame(gameID);

        if (Objects.equals(playerColor, "BLACK") && currGame.blackUsername() != null){

            throw new AlreadyTakenException("Error: already taken");

        } else if(Objects.equals(playerColor, "WHITE") && currGame.whiteUsername() != null){

            throw new AlreadyTakenException("Error: already taken");
        }

        try {

            if (Objects.equals(playerColor, "BLACK")) {

                GameData updatedGame = new GameData(gameID, currGame.whiteUsername(), username, gameName, currGame.game());

                db.put(updatedGame.gameID(), updatedGame);

                return updatedGame;

            } else {

                GameData updatedGame = new GameData(gameID, username, currGame.blackUsername(), gameName, currGame.game());

                db.put(updatedGame.gameID(), updatedGame);

                return updatedGame;
            }

        } catch (Exception e) {

            throw new DataAccessException("Error: player could not be added to the game");
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try {
            db.clear();

        } catch (Exception e) {

            throw new DataAccessException("Error: couldn't clear the database");
        }
    }
}
