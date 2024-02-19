package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class SQLUserDAO implements DataAccessObject<UserData>{

    private Connection connection = null;
    public SQLUserDAO(SQLConnector connector) throws DataAccessException{
        connection = connector.openConnection();
        initializeTable();
    }

    private void initializeTable() throws DataAccessException{

        String sql =
            """
            CREATE DATABASE IF NOT EXISTS chess;
            USE chess;
            CREATE TABLE IF NOT EXISTS users (
                uuid VARCHAR(36) NOT NULL UNIQUE,
                username VARCHAR(30) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                email VARCHAR(50),
                PRIMARY KEY (uuid)
            );
            """;

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
        }
        catch(SQLException e){
            throw new DataAccessException("Could not initialize table: " + e.getMessage());
        }
    }

    public void create(UserData data) throws DataAccessException {

        String sql =
            """
            INSERT INTO users (username, password_hash, email) VALUES 
            (?, ?, ?);
            """;

        try(PreparedStatement statement = connection.prepareStatement(sql)){

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
                users.username AS username,
                users.password_hash AS password,
                users.email AS email
            FROM
                users
            WHERE
                users.username = ?;
            """;

        try(PreparedStatement statement = connection.prepareStatement(sql)){

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

    }

    public void delete(String key) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }

    public Collection<UserData> getAll() throws DataAccessException {
        return null;
    }
}
