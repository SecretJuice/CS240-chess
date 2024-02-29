package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLAuthDAO extends SQLDataAccessObject implements DataAccessObject<AuthData> {

    public SQLAuthDAO() throws DataAccessException{

        super(
                """
                CREATE TABLE IF NOT EXISTS auths (
                    token VARCHAR(36) NOT NULL UNIQUE,
                    username VARCHAR(30) NOT NULL,
                    PRIMARY KEY (token),
                    FOREIGN KEY (username) REFERENCES users(username)
                );
                """);
    }
    public void create(AuthData data) throws DataAccessException {

        String sql =
                """
                INSERT INTO auths(token, username) 
                VALUES (?, ?);
                """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, data.authToken());
            statement.setString(2, data.username());

            statement.execute();
        }
        catch(SQLException e){

            if (e.getErrorCode() == 1062){
                throw new ItemAlreadyExistsException("Token already exists");
            }
            else{
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    public AuthData get(String key) throws DataAccessException {
        String sql =
                """
                SELECT
                    token,
                    username
                FROM
                    auths
                WHERE token = ?;
                """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, key);

            ResultSet results = statement.executeQuery();

            AuthData data = null;

            while(results.next()) {

                data = new AuthData(
                        results.getString("token"),
                        results.getString("username"));
            }

            return data;

        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void update(AuthData data) throws DataAccessException {

        String sql =
                """
                UPDATE auths SET
                username = ?
                WHERE token = ?;
                """;

        int resultCode = 0;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, data.username());
            statement.setString(2, data.authToken());

            resultCode = statement.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }

        if (resultCode == 0) {
            throw new ItemNotFoundException("Session doesn't exist");
        }
    }

    public void delete(String key) throws DataAccessException {

        String sql =
                """
                DELETE FROM auths WHERE token = ?;
                """;

        int resultCode = 0;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, key);

            resultCode = statement.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }

        if (resultCode == 0){
            throw new ItemNotFoundException("Session doesn't exist");
        }
    }

    public void clear() throws DataAccessException {

        String sql =
                """
                DELETE FROM auths;
                """;
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
        }
        catch(SQLException e){
            throw new DataAccessException("Could not clear table: " + e.getMessage());
        }
    }

    public Collection<AuthData> getAll() throws DataAccessException {

        String sql =
                """
                SELECT
                    token      AS authToken,
                    username   AS username
                FROM
                    auths;
                """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet results = statement.executeQuery();

            Collection<AuthData> data = new ArrayList<>();

            while(results.next()) {

                AuthData result = new AuthData(
                        results.getString("authToken"),
                        results.getString("username"));

                data.add(result);
            }
            return data;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
