package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AuthFactoryHashUsername;
import server.DataFactory;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class SQLAuthDAOTests {

    private final String url = "jdbc:mysql://localhost:3306/chess?user=root&password=password";
    private final DataFactory<AuthData> authFactory = new AuthFactoryHashUsername();

    @BeforeEach
    void initializeTests() throws DataAccessException {

        DataAccessObject<AuthData> authDAO = new SQLAuthDAO();
        DataAccessObject<UserData> userDAO = new SQLUserDAO();

        authDAO.clear();
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
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData newSession = authFactory.createData("TestUser");

            authDAO.create(newSession);
            Collection<AuthData> sessions = authDAO.getAll();

            assertTrue(sessions.contains(newSession), "Should contain newSession after being created");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("create(): Create Duplicate Record")
    void testDuplicateRecord(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData newSession = authFactory.createData("TestUser");

            authDAO.create(newSession);

            assertThrows(ItemAlreadyExistsException.class, () -> authDAO.create(newSession));
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("clear(): Successfully Cleared")
    void testClearRecords(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData newSession1 = authFactory.createData("TestUser1");
            AuthData newSession2 = authFactory.createData("TestUser2");

            authDAO.create(newSession1);
            authDAO.create(newSession2);

            authDAO.clear();

            Collection<AuthData> sessions = authDAO.getAll();

            assertTrue(sessions.isEmpty(), "sessions should be empty");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("getAll(): Successful Get All")
    void testGetAllRecords(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData newSession1 = authFactory.createData("TestUser1");
            AuthData newSession2 = authFactory.createData("TestUser2");

            authDAO.create(newSession1);
            authDAO.create(newSession2);

            Collection<AuthData> sessions = authDAO.getAll();

            assertTrue(sessions.contains(newSession1) && sessions.contains(newSession2), "Should contain all sessions created");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("getAll(): Empty")
    void testGetAllEmpty(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            Collection<AuthData> sessions = authDAO.getAll();

            assertTrue(sessions.isEmpty(), "Should contain no sessions");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("delete(): Successful Deletion")
    void testDeleteRecord(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData newSession1 = authFactory.createData("TestUser1");

            authDAO.create(newSession1);

            authDAO.delete(newSession1.authToken());

            Collection<AuthData> sessions = authDAO.getAll();

            assertTrue(sessions.isEmpty(), "delete() should remove created record");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("delete(): Delete Non-existent")
    void testDeleteNonExistent(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            assertThrows(ItemNotFoundException.class, () -> authDAO.delete("SomeNonexistentToken"), "Should throw ItemNotFoundException");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("update(): Successful Update")
    void testUpdateRecord(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData newSession1 = authFactory.createData("TestUser1");

            authDAO.create(newSession1);

            AuthData updatedSession = new AuthData(newSession1.authToken(), "TestUser2");

            authDAO.update(updatedSession);

            AuthData session = authDAO.get(newSession1.authToken());

            assertTrue(session.equals(updatedSession), "update() should change existing record");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("update(): Update Non-existent")
    void testUpdateNonExistent(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData fakeSession = authFactory.createData("FakeUser");

            assertThrows(ItemNotFoundException.class, () -> authDAO.update(fakeSession), "Should throw ItemNotFoundException");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("get(): Get Successfully")
    void testGetSuccessfully(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData newSession = authFactory.createData("TestUser1");
            authDAO.create(newSession);

            AuthData session = authDAO.get(newSession.authToken());

            assertTrue(newSession.equals(session), "Should return same object");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("get(): Get Non-existent")
    void testGetNonExistent(){

        try{
            DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

            AuthData session = authDAO.get("SoneNonexistentToken");

            assertNull(session, "Should return null");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }
}
