package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AuthFactoryHashUsername;
import server.DataFactory;
import server.services.ApplicationClearService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationClearTest {

    private DataAccessObject<UserData> userDAO = new LocalUserDAO();
    private DataAccessObject<AuthData> authDAO = new LocalAuthDAO();
    private DataAccessObject<GameData> gameDAO = new LocalGameDAO();
    private DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();

    private ApplicationClearService clearService = new ApplicationClearService(userDAO, authDAO, gameDAO);

    @BeforeEach
    void setUpTests() throws DataAccessException {

        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();

    }

    @Test
    @DisplayName("Populated becomes Empty")
    void testPopulatedBecomesEmpty(){

        try{
            populateDAOs();
            Collection<UserData> users = userDAO.getAll();
            Collection<AuthData> auths = authDAO.getAll();
            Collection<GameData> games = gameDAO.getAll();

            clearService.clearApplication();

            Collection<UserData> emptyUsers = new ArrayList<>();
            Collection<AuthData> emptyAuths = new ArrayList<>();
            Collection<GameData> emptyGames = new ArrayList<>();

            assertIterableEquals(emptyUsers, users, "Users should be empty after clearApplication()");
            assertIterableEquals(emptyAuths, auths, "Auths should be empty after clearApplication()");
            assertIterableEquals(emptyGames, games, "Games should be empty after clearApplication()");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    private void populateDAOs() throws DataAccessException{

        userDAO.create(new UserData("User1", "1234", "mail@email.com"));
        userDAO.create(new UserData("User2", "1234", "mail@email.com"));
        userDAO.create(new UserData("User3", "1234", "mail@email.com"));
        userDAO.create(new UserData("User4", "1234", "mail@email.com"));

        authDAO.create(authFactory.createData("User1"));
        authDAO.create(authFactory.createData("User2"));
        authDAO.create(authFactory.createData("User3"));
        authDAO.create(authFactory.createData("User4"));

        gameDAO.create(new GameData(1, "User1", "User2", "Game1", new ChessGame()));
        gameDAO.create(new GameData(2, "User3", "User4", "Game2", new ChessGame()));
    }

}
