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

    private UserDataAccess userDAO;
    private AuthDataAccess authDAO;
    private GameDataAccess gameDAO;

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
            Collection<UserData> users = userDAO.getAllUsers();
            Collection<AuthData> auths = authDAO.getAllAuths();
            Collection<GameData> games = gameDAO.getAllGames();

            clearService.clearApplication(userDAO, authDAO, gameDAO);

            Collection<UserData> emptyUsers = new ArrayList<>();
            Collection<AuthData> emptyAuths = new ArrayList<>();
            Collection<GameData> emptyGames = new ArrayList<>();

            assertIterableEquals(emptyUsers, users, "Users should be empty after clearApplication()");
            assertIterableEquals(emptyAuths, auths, "Auths should be empty after clearApplication()");
            assertIterableEquals(emptyGames, games, "Games should be empty after clearApplication()");

        }
        catch (Exception e){

        }

    }

    private void populateDAOs() throws DataAccessException{

        userDAO.createUser(new UserData("User1", "1234", "mail@email.com"));
        userDAO.createUser(new UserData("User2", "1234", "mail@email.com"));
        userDAO.createUser(new UserData("User3", "1234", "mail@email.com"));
        userDAO.createUser(new UserData("User4", "1234", "mail@email.com"));

        authDAO.createAuth(new AuthData(UUID.randomUUID().toString(), "User1"));
        authDAO.createAuth(new AuthData(UUID.randomUUID().toString(), "User2"));
        authDAO.createAuth(new AuthData(UUID.randomUUID().toString(), "User3"));
        authDAO.createAuth(new AuthData(UUID.randomUUID().toString(), "User4"));

        gameDAO.createGame(new GameData(1, "User1", "User2", "Game1", new ChessGame()));
        gameDAO.createGame(new GameData(2, "User3", "User4", "Game2", new ChessGame()));
    }

}
