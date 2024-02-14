package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.LocalGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.DataFactory;
import server.GameFactoryHashName;
import server.requests.BadRequestException;
import server.services.GameCreationService;

import static org.junit.jupiter.api.Assertions.*;

public class GameCreationTest {

    private DataAccessObject<GameData> gameDAO = new LocalGameDAO();

    private DataFactory<GameData> gameFactory = new GameFactoryHashName();

    @BeforeEach
    void setup() throws DataAccessException {

        gameDAO.clear();

    }

    @Test
    @DisplayName("Create Game Successfully")
    void createGameSuccessfully (){

        try {

            GameData testGame = gameFactory.createData("TestGame");

            GameData newGame = new GameCreationService(gameDAO, gameFactory).createGame("TestGame");

            assertEquals(testGame, newGame, "GameCreationService should return same GameData as test");

            GameData storedGame = gameDAO.get(Integer.toString(newGame.gameID()));

            assertEquals(testGame, storedGame, "Same GameData should be stored");

        }
        catch (Exception e){
            fail("Should not throw exceptions: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Game Already Exists")
    void gameAlreadyExistsTest() {

        GameData testGame = gameFactory.createData("TestGame");

        try {
            gameDAO.create(testGame);
        }
        catch (Exception e){
            fail("Test setup should not throw exceptions: " + e.getMessage());
        }

        assertThrows(BadRequestException.class, () -> new GameCreationService(gameDAO, gameFactory).createGame("TestGame"), "Should throw BadRequestException when trying to create a game that already exists");

    }



}
