package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.LocalAuthDAO;
import dataAccess.LocalUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AuthFactoryHashUsername;
import server.DataFactory;
import server.services.ServiceException;
import server.services.UserLoginService;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoginTest {

    private final DataAccessObject<UserData> userDAO =  new LocalUserDAO();
    private final DataAccessObject<AuthData> authDAO = new LocalAuthDAO();
    private final DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();

    @BeforeEach
    void setup() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();

        UserData newUserData = new UserData("NewUser", "password", "1234@mail.com");

        try{

            userDAO.create(newUserData);

        }
        catch(Exception e){
            fail("No exception should be thrown in test setup");
        }
    }

    @Test
    @DisplayName("Successful Login")
    void successfulLoginTest(){


        //Attempt Login

        try{
            UserData loginUser = new UserData("NewUser", "password", null);
            AuthData loginAuth = new UserLoginService().loginUser(loginUser, userDAO, authDAO, authFactory);

            AuthData testAuth = authFactory.createData(loginUser.username());

            assertNotNull(loginAuth, "UserLoginService should return AuthData");
            assertEquals(testAuth, loginAuth, "Auth must be for logged in user");

        }
        catch (ServiceException e){
            fail("Should not fail during test: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("User Doesn't Exist")
    void userDoesNotExist(){

        UserData loginUser = new UserData("NonexistentUser", "password", null);

        assertThrows(ServiceException.class, () -> new UserLoginService().loginUser(loginUser, userDAO, authDAO, authFactory), "Should throw ServiceException with message: 'User does not exist'");

    }

    @Test
    @DisplayName("Incorrect Password")
    void incorrectPassword(){

        UserData loginUser = new UserData("NewUser", "1234", null);

        assertThrows(ServiceException.class, () -> new UserLoginService().loginUser(loginUser, userDAO, authDAO, authFactory), "Should throw ServiceException with message: 'Password is incorrect'");

    }

}
