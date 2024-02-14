package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.LocalGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AuthFactoryHashUsername;
import server.DataFactory;
import server.requests.BadRequestException;
import server.requests.ForbiddenException;
import server.requests.JoinGameRequest;
import server.requests.UnauthorizedException;
import server.services.JoinGameService;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameTest {

    private DataAccessObject<GameData> gameDAO = new LocalGameDAO();
    private DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();

    @BeforeEach
    void setup() throws DataAccessException {

        gameDAO.clear();

    }

    @Test
    @DisplayName("Join Game Successfully")
    void joinGameSuccessfully(){

        try {

            //Setup Game
            GameData testGame = new GameData(123, null, null, "testGame", null);

            gameDAO.create(testGame);

            AuthData testSession = authFactory.createData("testUser");
            JoinGameRequest testRequest = new JoinGameRequest(123, ChessGame.TeamColor.BLACK);

            GameData newGame = new JoinGameService(gameDAO).joinGame(testRequest, testSession);

            assertNotNull(newGame, "JoinGameService should return GameData");
            assertTrue("testUser".equals(newGame.blackUsername()), "Black username in returned GameData should contain user");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Team Already Taken")
    void teamAlreadyTaken(){

        try {

            //Setup Game
            GameData testGame = new GameData(123, null, "AnotherUSer", "testGame", null);

            gameDAO.create(testGame);

            AuthData testSession = authFactory.createData("testUser");
            JoinGameRequest testRequest = new JoinGameRequest(123, ChessGame.TeamColor.BLACK);

            assertThrows(ForbiddenException.class, () -> new JoinGameService(gameDAO).joinGame(testRequest, testSession), "Should throw ForbiddenException because the desired slot is already taken");


        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Game Doesn't Exist")
    void gameDoesntExist(){

        try {
            AuthData testSession = authFactory.createData("testUser");
            JoinGameRequest testRequest = new JoinGameRequest(123, ChessGame.TeamColor.BLACK);

            assertThrows(BadRequestException.class, () ->  new JoinGameService(gameDAO).joinGame(testRequest, testSession), "Should throw BadRequestException because the game doesn't exist");
        }
        catch (Exception e){
            fail("Test setup should not throw exceptions: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("No session")
    void noSessionTest(){

        try {
            GameData testGame = new GameData(123, null, null, "testGame", null);

            gameDAO.create(testGame);

            AuthData testSession = null;
            JoinGameRequest testRequest = new JoinGameRequest(123, ChessGame.TeamColor.BLACK);

            assertThrows(UnauthorizedException.class, () ->  new JoinGameService(gameDAO).joinGame(testRequest, testSession), "Should throw UnauthorizedException because there's no session");
        }
        catch (Exception e){
            fail("Test setup should not throw exceptions: " + e.getMessage());
        }

    }


}
