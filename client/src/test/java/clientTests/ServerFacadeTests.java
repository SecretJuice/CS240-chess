package clientTests;

import data.requests.ForbiddenException;
import data.requests.UnauthorizedException;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.AuthFactoryHashUsername;
import server.DataFactory;
import server.GameFactoryHashName;
import server.Server;
import web.ServerFacade;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    private DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();
    private DataFactory<GameData> gameFactory = new GameFactoryHashName();

    private DataAccessObject<UserData> userDAO = new LocalUserDAO();
    private DataAccessObject<AuthData> authDAO = new LocalAuthDAO();
    private DataAccessObject<GameData> gameDAO = new LocalGameDAO();

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
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void testLoginSuccessful() {

        ServerFacade serverFacade = new ServerFacade();
        try{

            UserData testUser = new UserData("TestUser", "TestPassword", null);
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

    @Test
    public void testLogoutSuccessful(){

        ServerFacade serverFacade = new ServerFacade();

        try{

            AuthData session = authFactory.createData("TestUser");
            authDAO.create(session);

            serverFacade.setSession(session);
            serverFacade.logout();

            Collection<AuthData> sessions = authDAO.getAll();

            assertTrue(sessions.isEmpty(), "Sessions should not contain auth for user");
        }
        catch(Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testLogoutNonexistent() {

        ServerFacade serverFacade = new ServerFacade();

        AuthData session = authFactory.createData("TestUser");

        serverFacade.setSession(session);

        assertThrows(UnauthorizedException.class, serverFacade::logout, "Should throw UnauthorizedException");

    }

    @Test
    public void testLogoutNoSession() {

        ServerFacade serverFacade = new ServerFacade();

        assertThrows(UnauthorizedException.class, serverFacade::logout, "Should throw UnauthorizedException");

    }

    @Test public void testCreateGameSuccessfully(){

        ServerFacade serverFacade = new ServerFacade();

        try{
            makeSession(serverFacade, "TestUser");

            Integer gameID = serverFacade.createGame("TestGame");

            assertNotNull(gameID, "Should return gameID");
        }
        catch(Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test public void testCreateGameBadSession(){

        ServerFacade serverFacade = new ServerFacade();

        assertThrows(UnauthorizedException.class, () -> {
            serverFacade.createGame("TestGame");
        }, "Should throw UnauthorizedException");
    }

    @Test
    public void testListGames(){

        ServerFacade serverFacade = new ServerFacade();

        try{
            makeSession(serverFacade, "TestUser");

            GameData game1 = gameFactory.createData("Game1");
            GameData game2 = gameFactory.createData("Game2");

            gameDAO.create(game1);
            gameDAO.create(game2);

            Collection<GameData> games = new ArrayList<>( serverFacade.listGames() );

            boolean game1Found = false;
            boolean game2Found = false;

            for(GameData game : games){

                if (game.equals(game1)){
                    game1Found = true;
                }

                if (game.equals(game2)){
                    game2Found = true;
                }
            }

            assertTrue(game1Found && game2Found, "Returned list should contain both created games");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    public void testListGamesEmpty(){

        ServerFacade serverFacade = new ServerFacade();

        try{
            makeSession(serverFacade, "TestUser");

            Collection<GameData> games = new ArrayList<>( serverFacade.listGames() );

            assertTrue(games.isEmpty(), "Returned list should be empty");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test public void testListGamesBadSession(){

        ServerFacade serverFacade = new ServerFacade();

        try{
            makeBadSession(serverFacade, "BadUser");

            assertThrows(UnauthorizedException.class, serverFacade::listGames, "Should throw UnauthorizedException");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }



    private void makeSession(ServerFacade facade, String username) throws Exception{
        AuthData session = authFactory.createData(username);
        authDAO.create(session);
        facade.setSession(session);
    }

    private void makeBadSession(ServerFacade facade, String username) {
        AuthData session = authFactory.createData(username);
        facade.setSession(session);
    }


}
