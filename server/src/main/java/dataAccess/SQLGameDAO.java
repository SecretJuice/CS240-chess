package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO extends SQLDataAccessObject implements DataAccessObject<GameData>{

    private final Gson jsonParser = new Gson();

    public SQLGameDAO() throws DataAccessException{

        super(
                """
                CREATE TABLE IF NOT EXISTS games (
                    gameID INTEGER UNIQUE,
                    name VARCHAR(50) NOT NULL,
                    white_player VARCHAR(36),
                    black_player VARCHAR(36),
                    state JSON,
                    PRIMARY KEY (gameID)
                );
                """);
    }


    public void create(GameData data) throws DataAccessException {

        String sql =
                """
                INSERT INTO games (gameID, name, white_player, black_player, state)
                VALUES (?, ?, ?, ?, ?);
                """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, data.gameID());
            statement.setString(2, data.gameName());
            statement.setString(3, data.whiteUsername());
            statement.setString(4, data.blackUsername());
            statement.setString(5, jsonParser.toJson(data.game()));

            statement.execute();
        }
        catch(SQLException e){

            if (e.getErrorCode() == 1062){
                throw new ItemAlreadyExistsException("Game already exists");
            }
            else{
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    public GameData get(String key) throws DataAccessException {

        String sql =
                """
                SELECT
                    games.gameID AS gameID,
                    games.name AS gameName,
                    games.white_player AS white_player,
                    games.black_player AS black_player,
                    games.state AS game
                FROM
                    games
                WHERE games.gameID = ?;
                """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, Integer.parseInt(key));

            ResultSet results = statement.executeQuery();

            GameData data = null;


            while(results.next()) {
                ChessGame game = jsonParser.fromJson(results.getString("game"), ChessGame.class);

                data = new GameData(
                        results.getInt("gameID"),
                        results.getString("gameName"),
                        results.getString("white_username"),
                        results.getString("black_username"),
                        game);
            }

            return data;

        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void update(GameData data) throws DataAccessException {

        String sql =
                """
                UPDATE games SET
                    name = ?,
                    white_player = ?,
                    black_player = ?,
                    state = ?
                WHERE gameID = ?;
                """;

        int resultCode = 0;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, data.gameName());
            statement.setString(2, data.whiteUsername());
            statement.setString(3, data.blackUsername());
            statement.setString(4, jsonParser.toJson(data.game()));

            statement.setInt(5, data.gameID());

            resultCode = statement.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }

        if (resultCode == 0) {
            throw new ItemNotFoundException("Game doesn't exist");
        }
    }

    public void delete(String key) throws DataAccessException {

        String sql =
                """
                DELETE FROM games WHERE gameID = ?;
                """;

        int resultCode = 0;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, Integer.parseInt(key));

            resultCode = statement.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }

        if (resultCode == 0){
            throw new ItemNotFoundException("Game doesn't exist");
        }
    }

    public void clear() throws DataAccessException {
        String sql =
                """
                DELETE FROM games;
                """;
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
        }
        catch(SQLException e){
            throw new DataAccessException("Could not clear table: " + e.getMessage());
        }
    }

    public Collection<GameData> getAll() throws DataAccessException {

        String sql =
                """
                SELECT
                    games.gameID AS gameID,
                    games.name AS gameName,
                    games.white_player AS white_player,
                    games.black_player AS black_player,
                    games.state AS game
                FROM
                    games;
                """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet results = statement.executeQuery();

            Collection<GameData> data = new ArrayList<>();

            while(results.next()) {

                ChessGame game = jsonParser.fromJson(results.getString("game"), ChessGame.class);

                GameData result = new GameData(
                        results.getInt("gameID"),
                        results.getString("gameName"),
                        results.getString("white_username"),
                        results.getString("black_username"),
                        game);

                data.add(result);
            }
            return data;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
