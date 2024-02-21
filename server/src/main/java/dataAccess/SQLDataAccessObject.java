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

    public SQLDataAccessObject(String connectionUrl, String tableDDL) throws DataAccessException{
        URL = connectionUrl;
        tableSQL = tableDDL;
        initializeTable();
    }

    protected Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL);
    }

    private void initializeTable() throws DataAccessException{

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

    protected PreparedStatement prepareSQL(String sql) throws SQLException{
        Connection connection = getConnection();
        return connection.prepareStatement(sql);
    }

}
