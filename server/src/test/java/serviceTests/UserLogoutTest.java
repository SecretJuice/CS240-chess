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
import server.services.UserLogoutService;
import server.services.UserRegistrationService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserLogoutTest {

    private DataAccessObject<AuthData> authDOA = new LocalAuthDAO();
    private DataAccessObject<UserData> userDOA = new LocalUserDAO();
    private DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();

    @BeforeEach
    void setup() throws DataAccessException {
        authDOA.clear();
        userDOA.clear();
    }

    @Test
    @DisplayName("Successful Logout")
    void successfulLogout() {

        AuthData testAuth = null;

        try {

            UserData testUser = new UserData("NewUser", "password", "1234@1234.com");
            testAuth = new UserRegistrationService().registerUser(testUser, userDOA, authDOA, authFactory);

            assertNotNull(testAuth, "Need to initialize Auth");

            new UserLogoutService().logoutUser(testAuth, authDOA);

            Collection<AuthData> sessions = new ArrayList<>(authDOA.getAll());

            assertFalse(sessions.contains(testAuth), "Auth shouldn't be stored after logout");
        }
        catch (Exception e){
            fail("Exception should not be thrown during test: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Session Doesn't Exist")
    void sessionDoesntExist(){

        AuthData testAuth = authFactory.createData("NewUser");

        assertThrows(ServiceException.class, () -> new UserLogoutService().logoutUser(testAuth, authDOA));

    }

}
