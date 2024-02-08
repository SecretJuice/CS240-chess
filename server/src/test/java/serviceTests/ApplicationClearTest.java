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
import server.services.ApplicationClearService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationClearTest {

    private DataAccessObject<UserData> userDAO;
    private DataAccessObject<AuthData> authDAO;
    private DataAccessObject<GameData> gameDAO;

    private ApplicationClearService clearService;

    @BeforeEach
    void setUpTests() {
        userDAO = new LocalUserDAO();
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();

        clearService = new ApplicationClearService();
    }

    @Test
    @DisplayName("Populated becomes Empty")
    void testPopulatedBecomesEmpty(){

        try{
            populateDAOs();
            Collection<UserData> users = userDAO.getAll();
            Collection<AuthData> auths = authDAO.getAll();
            Collection<GameData> games = gameDAO.getAll();

            clearService.clearApplication(userDAO, authDAO, gameDAO);

            Collection<UserData> emptyUsers = new ArrayList<>();
            Collection<AuthData> emptyAuths = new ArrayList<>();
            Collection<GameData> emptyGames = new ArrayList<>();

            assertIterableEquals(emptyUsers, users, "Users should be empty after clearApplication()");
            assertIterableEquals(emptyAuths, auths, "Auths should be empty after clearApplication()");
            assertIterableEquals(emptyGames, games, "Games should be empty after clearApplication()");

        }
        catch (DataAccessException e){
            fail("Should not throw exception");
        }

    }

    private void populateDAOs() throws DataAccessException{

        userDAO.create(new UserData("User1", "1234", "mail@email.com"));
        userDAO.create(new UserData("User2", "1234", "mail@email.com"));
        userDAO.create(new UserData("User3", "1234", "mail@email.com"));
        userDAO.create(new UserData("User4", "1234", "mail@email.com"));

        authDAO.create(new AuthData(UUID.randomUUID().toString(), "User1"));
        authDAO.create(new AuthData(UUID.randomUUID().toString(), "User2"));
        authDAO.create(new AuthData(UUID.randomUUID().toString(), "User3"));
        authDAO.create(new AuthData(UUID.randomUUID().toString(), "User4"));

        gameDAO.create(new GameData(1, "User1", "User2", "Game1", new ChessGame()));
        gameDAO.create(new GameData(2, "User3", "User4", "Game2", new ChessGame()));
    }

}
