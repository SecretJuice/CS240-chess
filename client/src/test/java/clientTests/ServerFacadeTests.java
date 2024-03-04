package clientTests;

import data.requests.ForbiddenException;
import data.requests.UnauthorizedException;
import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.LocalUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import web.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(7777, true);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void beforeEach() throws DataAccessException {
        DataAccessObject<UserData> userDAO = new LocalUserDAO();
        userDAO.clear();
    }

    @Test
    public void testLoginSuccessful() {

        ServerFacade serverFacade = new ServerFacade();
        try{

            UserData testUser = new UserData("TestUser", "TestPassword", null);
            DataAccessObject<UserData> userDAO = new LocalUserDAO();
            userDAO.create(testUser);

            String result = serverFacade.login("TestUser", "TestPassword");

            assertEquals(testUser.username(), result);
        }
        catch(Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }
    @Test
    public void testLoginNonexistant() {

        ServerFacade serverFacade = new ServerFacade();

        assertThrows(UnauthorizedException.class, () -> serverFacade.login("NonexistentUser", "BadPassword"));

    }

    @Test
    public void testRegisterSuccessful(){

        ServerFacade serverFacade = new ServerFacade();

        try{

            String result = serverFacade.register("TestUser", "TestPassword", "test@test.com");

            assertEquals("TestUser", result, "Should return username of newly registered user");
        }
        catch(Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterExistent(){

        ServerFacade serverFacade = new ServerFacade();

        try{

            String testUsername = serverFacade.register("TestUser", "TestPassword", "test@test.com");

            assertThrows(ForbiddenException.class, () -> {
                        serverFacade.register("TestUser", "AnotherPassword", "test@test2.com");
                    }, "Should throw ForbiddenException");
        }
        catch(Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }
    }

}
