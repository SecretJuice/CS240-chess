package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.LocalAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AuthFactoryHashUsername;
import server.DataFactory;
import server.services.AuthenticationService;
import server.services.ServiceException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {

    private DataAccessObject<AuthData> authDAO = new LocalAuthDAO();
    private DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();

    @BeforeEach
    void setup() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    @DisplayName("Valid Session")
    void validSessionTest() {

        try {

            AuthData testAuth = authFactory.createData("TestUser");

            authDAO.create(testAuth);

            AuthData validSession = new AuthenticationService().authenticateSession(testAuth.authToken(), authDAO);

            assertNotNull(validSession, "AuthenticationService should return AuthData");
            assertEquals(testAuth, validSession, "AuthData returned by AuthenticationService should match test AuthData");

        }
        catch (Exception e){
            fail("Should not throw exception during valid session test: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Invalid Session")
    void invalidSessionTest() {

        assertThrows(ServiceException.class, () -> new AuthenticationService().authenticateSession("faketoken", authDAO));

    }
}
