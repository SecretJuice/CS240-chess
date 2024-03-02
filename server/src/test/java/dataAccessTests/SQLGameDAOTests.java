package dataAccessTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.DataFactory;
import server.GameFactoryHashName;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class SQLGameDAOTests {
    private final DataFactory<GameData> gameFactory = new GameFactoryHashName();

    @BeforeEach
    void initializeTests() throws DataAccessException {

        DataAccessObject<GameData> gameDAO = new SQLGameDAO();
        DataAccessObject<UserData> userDAO = new SQLUserDAO();

        gameDAO.clear();
        userDAO.clear();

        UserData newUser = new UserData("TestUser", "password", "test@test.com");
        UserData newUser1 = new UserData("TestUser1", "password", "test@test.com");
        UserData newUser2 = new UserData("TestUser2", "password", "test@test.com");

        userDAO.create(newUser);
        userDAO.create(newUser1);
        userDAO.create(newUser2);

    }

    @Test
    @DisplayName("create(): Successful Creation")
    void testCreateRecord(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData newGame = gameFactory.createData("Test Game");

            gameDAO.create(newGame);
            Collection<GameData> games = gameDAO.getAll();

            assertTrue(games.contains(newGame), "Should contain newGame after being created");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("create(): Create Duplicate Record")
    void testDuplicateRecord(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData newGame = gameFactory.createData("Test Game");

            gameDAO.create(newGame);

            assertThrows(ItemAlreadyExistsException.class, () -> gameDAO.create(newGame));
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("clear(): Successfully Cleared")
    void testClearRecords(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData newGame1 = gameFactory.createData("Test Game 1");
            GameData newGame2 = gameFactory.createData("Test Game 2");

            gameDAO.create(newGame1);
            gameDAO.create(newGame2);

            gameDAO.clear();

            Collection<GameData> games = gameDAO.getAll();

            assertTrue(games.isEmpty(), "games should be empty");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("getAll(): Successful Get All")
    void testGetAllRecords(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData newGame1 = gameFactory.createData("Test Game 1");
            GameData newGame2 = gameFactory.createData("Test Game 2");

            gameDAO.create(newGame1);
            gameDAO.create(newGame2);

            Collection<GameData> games = gameDAO.getAll();

            assertTrue(games.contains(newGame1) && games.contains(newGame2), "Should contain all games created");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("getAll(): Empty")
    void testGetAllEmpty(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            Collection<GameData> games = gameDAO.getAll();

            assertTrue(games.isEmpty(), "Should contain no games");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("delete(): Successful Deletion")
    void testDeleteRecord(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData newGame1 = gameFactory.createData("Test Game 1");

            gameDAO.create(newGame1);

            gameDAO.delete(Integer.toString(newGame1.gameID()));

            Collection<GameData> games = gameDAO.getAll();

            assertTrue(games.isEmpty(), "delete() should remove created record");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("delete(): Delete Non-existent")
    void testDeleteNonExistent(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            assertThrows(ItemNotFoundException.class, () -> gameDAO.delete("999999"), "Should throw ItemNotFoundException");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("update(): Successful Update")
    void testUpdateRecord(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData newGame1 = gameFactory.createData("Test Game 1");

            gameDAO.create(newGame1);

            GameData updatedGame = new GameData(newGame1.gameID(), "TestUser2", "TestUser1", newGame1.gameName(), newGame1.game());

            gameDAO.update(updatedGame);

            GameData game = gameDAO.get(Integer.toString(newGame1.gameID()));

            assertTrue(game.equals(updatedGame), "update() should change existing record");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("update(): Update Non-existent")
    void testUpdateNonExistent(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData fakeGame = gameFactory.createData("Fake Game");

            assertThrows(ItemNotFoundException.class, () -> gameDAO.update(fakeGame), "Should throw ItemNotFoundException");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("get(): Get Successfully")
    void testGetSuccessfully(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData newGame = gameFactory.createData("Test Game");
            gameDAO.create(newGame);

            GameData session = gameDAO.get(Integer.toString(newGame.gameID()));

            assertTrue(newGame.equals(session), "Should return same object");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("get(): Get Non-existent")
    void testGetNonExistent(){

        try{
            DataAccessObject<GameData> gameDAO = new SQLGameDAO();

            GameData session = gameDAO.get("124141234");

            assertNull(session, "Should return null");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }
}
