package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.ListGameData;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

//Placeholder class for Phase4 when I'll interact with the database and store GameData there
public class MySqlGameDAO implements GameDAO {


    private int executeUpdate(String statement, Object... params) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {

            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {

                for (var i = 0; i < params.length; i++) {

                    var param = params[i];

                    if (param instanceof String p) ps.setString(i + 1, p);

                    else if (param instanceof Integer p) ps.setInt(i + 1, p);

                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());

                    else if (param == null) ps.setNull(i + 1, NULL);
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
