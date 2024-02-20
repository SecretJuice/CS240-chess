package dbTests;

import dataAccess.DataAccessObject;
import dataAccess.SQLConnector;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class SQLUserDAOTests {

    @Test
    @DisplayName("Successful Creation")
    void testCreateRecord(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO(new SQLConnector());

            UserData newUser = new UserData("TestUser", "password", "test@test.com");

            userDAO.create(newUser);
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

}
