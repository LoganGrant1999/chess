package dataaccess;

import chess.ChessGame;
import exceptions.AlreadyTakenException;
import exceptions.MissingDataException;
import model.GameData;
import model.ListGameData;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

//DAO class that interacts with game table in the database
public class MySqlGameDAO implements GameDAO {

    // Method that takes in a SQL statement and parameters and executes updates within the user table directly
    private int executeUpdate(String statement, Object... params) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {

            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {

                for (var i = 0; i < params.length; i++) {

                    var param = params[i];

                    if (param instanceof String p) {

                        ps.setString(i + 1, p);
                    }

                    else if (param instanceof Integer p) {

                        ps.setInt(i + 1, p);
                    }

                    else if (param instanceof ChessGame p) {

                        ps.setString(i + 1, p.toString());
                    }

                    else if (param == null) {

                        ps.setNull(i + 1, NULL);
                    }
                }

                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();

                if (rs.next()) {

                    return rs.getInt(1);

                }

                return 0;
            }

        } catch (SQLException e) {

            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {

        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, chessGame=? WHERE gameID=?";

        var json = new Gson().toJson(gameData.game());

        executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), json, gameID);

    }

    //method designed to create a new record of game data in the game table
    @Override
    public int createGame(String gameName) throws DataAccessException {

        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";

        var json  = new Gson().toJson(new ChessGame());

        return executeUpdate(statement, null, null, gameName, json);

    }

    //Method designed to retrieve game data for a game given a valid gameID
    @Override
    public GameData getGame(int gameID) throws DataAccessException {

        if (gameID == 0) {

            throw new MissingDataException("Error: Bad Request");
        }

        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT * FROM game WHERE gameID=?";

            try (var ps = conn.prepareStatement(statement)) {

                ps.setInt(1, gameID);

                try (var rs = ps.executeQuery()) {

                    if (rs.next()) {

                        int id = rs.getInt("gameID");

                        String white = rs.getString("whiteUsername");

                        String black = rs.getString("blackUsername");

                        String gameName = rs.getString("gameName");

                        String chessGame = rs.getString("chessGame");

                        ChessGame game = new Gson().fromJson(chessGame, ChessGame.class);

                        return new GameData(id, white, black, gameName, game);
                    }
                }
            }
        } catch (SQLException e) {

            throw new DataAccessException(e.getMessage());
        }

        return null;
    }

    //Method designed to return a list of all the games in the game table given a valid authToken
    @Override
    public ArrayList<ListGameData> listGames(String authToken) throws DataAccessException {

        if (authToken == null){

            throw new MissingDataException("Error: Bad Request");
        }

        var result = new ArrayList<ListGameData>();

        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName FROM game";

            try (var ps = conn.prepareStatement(statement)) {

                try (var rs = ps.executeQuery()) {

                    while (rs.next()) {

                        ListGameData listGameData = new ListGameData(rs.getInt("gameID"),

                                rs.getString("whiteUsername"),

                                rs.getString("blackUsername"),

                                rs.getString("gameName"));

                        result.add(listGameData);
                    }
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(e.getMessage());
        }

        return result;
    }

    //Method designed to enable user's with a valid username to join a game in the game table as a specified team color
    @Override
    public GameData joinGame(int gameID, String username, String playerColor, String gameName) throws DataAccessException {

        if (getGame(gameID) == null) {

            throw new DataAccessException("Error: game does not exist");
        }

        GameData currGame = getGame(gameID);

        if (Objects.equals(playerColor, "BLACK") && currGame.blackUsername() != null) {

            throw new AlreadyTakenException("Error: already taken");

        } else if (Objects.equals(playerColor, "WHITE") && currGame.whiteUsername() != null) {

            throw new AlreadyTakenException("Error: already taken");
        }

        if (Objects.equals(playerColor, "BLACK")) {

            var statement = "UPDATE game SET blackUsername = ? WHERE gameID = ?";

            executeUpdate(statement, username, gameID);

            return new GameData(currGame.gameID(), currGame.whiteUsername(), username, gameName, currGame.game());

        } else {

            var statement = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";

            executeUpdate(statement, username, gameID);

            return new GameData(currGame.gameID(), username, currGame.blackUsername(), gameName, currGame.game());

        }
    }


    //method designed to truncate the game table
    @Override
    public void clear() throws DataAccessException {

        var statement = "TRUNCATE game";

        executeUpdate(statement);
    }
}
