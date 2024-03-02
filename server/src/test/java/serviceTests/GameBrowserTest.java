package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.LocalGameDAO;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.services.GameBrowserService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameBrowserTest {

    private DataAccessObject<GameData> gameDAO;

    @BeforeEach
    void setup() throws DataAccessException{
        gameDAO = new LocalGameDAO();
        gameDAO.clear();
    }

    @Test
    @DisplayName("All Games")
    void allGames(){

        try{
            gameDAO.create(new GameData(1, "white", "black", "testgame", new ChessGame()));
            gameDAO.create(new GameData(2, "connor", "chad", "connorvchad", new ChessGame()));
            gameDAO.create(new GameData(3, "trump", "biden", "oh boy", new ChessGame()));

            Collection<GameData> testGames = new ArrayList<>(gameDAO.getAll());

            Collection<GameData> games = new GameBrowserService(gameDAO).getGameList();

            assertFalse(games.isEmpty(), "GameBrowserService should return a collection of GameData");
            assertIterableEquals(testGames, games, "Contents of returned collection should match");
        }
        catch (Exception e){
            fail("Should not throw exception on All Games test: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Empty Games List")
    void emptyGamesListTest(){

        try{

            Collection<GameData> games = new GameBrowserService(gameDAO).getGameList();

            assertTrue(games.isEmpty(), "GameBrowserService.getGameList() should return an empty collection");
        }
        catch (Exception e){
            fail("Should not throw exception during test: " + e.getMessage());
        }

    }

}
