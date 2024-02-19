package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

public class SQLConnector{

    private String connectionURL = "jdbc:mysql://localhost:3306/chess?user=root&password=password";

    public SQLConnector(){}
    public SQLConnector(String url){
        connectionURL = url;
    }

    protected Connection openConnection() throws DataAccessException{

        try(Connection c = DriverManager.getConnection(connectionURL)){
            return c;
        }
        catch(SQLException e){
            throw new DataAccessException("Could not connect to database: " + e.getMessage());
        }

    }
}
