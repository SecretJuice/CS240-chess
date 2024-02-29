package dataAccess;

import model.UserData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class SQLUserDAO extends SQLDataAccessObject implements DataAccessObject<UserData>{

    public SQLUserDAO() throws DataAccessException{

        super(
                """
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(30) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    email VARCHAR(50),
                    PRIMARY KEY (username)
                );
                """);
    }


    public void create(UserData data) throws DataAccessException {

        String sql =
            """
            INSERT INTO users (username, password_hash, email) 
            VALUES (?, ?, ?);
            """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, data.username());
            statement.setString(2, data.password());
            statement.setString(3, data.email());

            statement.execute();
        }
        catch(SQLException e){

            if (e.getErrorCode() == 1062){
                throw new ItemAlreadyExistsException("User already exists");
            }
            else{
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    public UserData get(String key) throws DataAccessException {

        String sql =
            """
            SELECT
                users.username      AS username,
                users.password_hash AS password,
                users.email         AS email
            FROM
                users
            WHERE
                users.username = ?;
            """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, key);

            ResultSet results = statement.executeQuery();

            UserData data = null;

            while(results.next()) {

                data = new UserData(
                        results.getString("username"),
                        results.getString("password"),
                        results.getString("email"));
            }

            return data;

        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void update(UserData data) throws DataAccessException {

        String sql =
                """
                UPDATE users SET
                password_hash = ?, email = ?
                WHERE username = ?;
                """;

        int resultCode = 0;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, data.password());
            statement.setString(2, data.email());

            statement.setString(3, data.username());

            resultCode = statement.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }

        if (resultCode == 0) {
            throw new ItemNotFoundException("User doesn't exist");
        }
    }

    public void delete(String key) throws DataAccessException {

        String sql =
                """
                DELETE FROM users WHERE username = ?;
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
            throw new ItemNotFoundException("User doesn't exist");
        }
    }

    public void clear() throws DataAccessException {
        String sql =
                """
                DELETE FROM users;
                """;
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
        }
        catch(SQLException e){
            throw new DataAccessException("Could not clear table: " + e.getMessage());
        }
    }

    public Collection<UserData> getAll() throws DataAccessException {

        String sql =
                """
                SELECT
                    users.username      AS username,
                    users.password_hash AS password,
                    users.email         AS email
                FROM
                    users;
                """;

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet results = statement.executeQuery();

            Collection<UserData> data = new ArrayList<>();

            while(results.next()) {

                UserData result = new UserData(
                        results.getString("username"),
                        results.getString("password"),
                        results.getString("email"));

                data.add(result);
            }
            return data;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
