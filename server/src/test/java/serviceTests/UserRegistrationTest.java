package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AuthFactory;
import server.AuthFactoryHashUsername;
import server.services.ServiceException;
import server.services.UserRegistrationService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationTest {

    private DataAccessObject<UserData> userDAO;
    private DataAccessObject<AuthData> authDAO;
    private AuthFactory authFactory;

    @BeforeEach
    void setup() throws DataAccessException{
        userDAO = new LocalUserDAO();
        authDAO = new LocalAuthDAO();
        authFactory = new AuthFactoryHashUsername();

        userDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Register New User")
    void registerUserTest() throws DataAccessException {

        UserData newUser = new UserData("MyNewUser", "1234", "user@email.com");

        AuthData newUserAuth;

        try{
           newUserAuth = new UserRegistrationService().registerUser(newUser, userDAO, authDAO, authFactory);

        }catch (Exception e){
            fail(e.getMessage());
            newUserAuth = null;
        }


        assertTrue(userDAO.getAll().contains(newUser), "Users must contain newly registered user");
        assertEquals(userDAO.get(newUser.username()), newUser, "User must be gettable by username");

        assertNotNull(newUserAuth, "Must return AuthData");
        assertEquals(newUserAuth, authFactory.createAuthData(newUser.username()), "Auth token must match output from factory");

    }

    @Test
    @DisplayName("User Already Exists")
    void userExistsTest(){

        UserData newUser = new UserData("MyNewUser", "1234", "user@email.com");

        try{
            userDAO.create(newUser);
        }catch (Exception e){}

        assertThrows(ServiceException.class, () -> new UserRegistrationService().registerUser(newUser, userDAO, authDAO, authFactory), "Should throw a ServiceException");

        AuthData authData;
        Collection<AuthData> sessions = new ArrayList<>();

        try{
            authData = authFactory.createAuthData(newUser.username());
            sessions.addAll(authDAO.getAll());

        }
        catch (DataAccessException e){
            fail("Test AuthData creation should not throw exception");
            authData = null;
        }

        assertFalse(sessions.contains(authData), "New Auth should not be created for already existing user");

    }

}
