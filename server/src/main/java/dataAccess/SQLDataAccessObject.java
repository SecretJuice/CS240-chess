package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class SQLDataAccessObject {

    protected String tableSQL =
            """
               
            """;

    public SQLDataAccessObject(String tableDDL) throws DataAccessException{
        tableSQL = tableDDL;
        DatabaseManager.createDatabase();
        initializeTable();
    }

    protected Connection getConnection() throws DataAccessException{
        return DatabaseManager.getConnection();
    }

    private void initializeTable() throws DataAccessException{

        try(Connection connection = getConnection()){

            PreparedStatement statement = connection.prepareStatement(tableSQL);
            statement.execute();

        }
        catch(SQLException e){
            throw new DataAccessException("Could not initialize table: " + e.getMessage());
        }
    }


}
