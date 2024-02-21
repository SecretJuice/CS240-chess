package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class SQLDataAccessObject {

    private String URL = "";
    protected String dbSQL =
            """
            CREATE DATABASE IF NOT EXISTS chess;
            """;
    protected String tableSQL =
            """
               
            """;

    public SQLDataAccessObject(String connectionUrl) throws DataAccessException{
        URL = connectionUrl;
        initializeTable();
    }

    protected Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL);
    }

    private void initializeTable() throws DataAccessException{

        String dbSQL =
                """
                CREATE DATABASE IF NOT EXISTS chess;
                """;
        String tableSQL =
                """    
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(30) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    email VARCHAR(50),
                    PRIMARY KEY (username)
                );
                """;

        try(Connection connection = getConnection()

        ){
            PreparedStatement statement = connection.prepareStatement(dbSQL);
            statement.execute();

            connection.setCatalog("chess");

            statement = connection.prepareStatement(tableSQL);
            statement.execute();

        }
        catch(SQLException e){
            throw new DataAccessException("Could not initialize table: " + e.getMessage());
        }
    }

}
