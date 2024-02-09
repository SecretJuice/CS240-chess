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
import server.services.GameCreationService;
import server.services.ServiceException;

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

            GameData newGame = new GameCreationService().createGame("TestGame", gameDAO, gameFactory);

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

        assertThrows(ServiceException.class, () -> new GameCreationService().createGame("TestGame", gameDAO, gameFactory));

    }

}
