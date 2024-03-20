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
import data.requests.UnauthorizedException;
import server.services.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {

    private DataAccessObject<AuthData> authDAO;
    private DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();

    @BeforeEach
    void setup() throws DataAccessException {

        authDAO = new LocalAuthDAO();
        authDAO.clear();
    }

    @Test
    @DisplayName("Valid Session")
    void validSessionTest() {

        try {

            AuthData testAuth = authFactory.createData("TestUser");

            authDAO.create(testAuth);

            AuthData validSession = new AuthenticationService(authDAO).authenticateSession(testAuth.authToken());

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

        assertThrows(UnauthorizedException.class, () -> new AuthenticationService(authDAO).authenticateSession("faketoken"));

    }
}
