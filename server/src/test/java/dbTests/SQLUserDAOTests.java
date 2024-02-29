package dbTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTests {


    @BeforeEach
    void initializeTests() throws DataAccessException {

        DataAccessObject<UserData> userDAO = new SQLUserDAO();
        DataAccessObject<AuthData> authDAO = new SQLAuthDAO();

        authDAO.clear();
        userDAO.clear();

    }

    @Test
    @DisplayName("create(): Successful Creation")
    void testCreateRecord(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData newUser = new UserData("TestUser", "password", "test@test.com");

            userDAO.create(newUser);
            Collection<UserData> users = userDAO.getAll();

            assertTrue(users.contains(newUser), "Should contain newUser after being created");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("create(): Create Duplicate Record")
    void testDuplicateRecord(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData newUser = new UserData("TestUser", "password", "test@test.com");

            userDAO.create(newUser);

            assertThrows(ItemAlreadyExistsException.class, () -> userDAO.create(newUser));
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("clear(): Successfully Cleared")
    void testClearRecords(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData newUser1 = new UserData("TestUser1", "password", "test@test.com");
            UserData newUser2 = new UserData("TestUser2", "password", "test@test.com");

            userDAO.create(newUser1);
            userDAO.create(newUser2);

            userDAO.clear();

            Collection<UserData> users = userDAO.getAll();

            assertTrue(users.isEmpty(), "users should be empty");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("getAll(): Successful Get All")
    void testGetAllRecords(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData newUser1 = new UserData("TestUser1", "password", "test@test.com");
            UserData newUser2 = new UserData("TestUser2", "password", "test@test.com");
            UserData newUser3 = new UserData("TestUser3", "password", "test@test.com");

            userDAO.create(newUser1);
            userDAO.create(newUser2);
            userDAO.create(newUser3);

            Collection<UserData> users = userDAO.getAll();

            assertTrue(users.contains(newUser1) && users.contains(newUser2) && users.contains(newUser3), "Should contain all users created");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("getAll(): Empty")
    void testGetAllEmpty(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            Collection<UserData> users = userDAO.getAll();

            assertTrue(users.isEmpty(), "Should contain no users");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("delete(): Successful Deletion")
    void testDeleteRecord(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData newUser1 = new UserData("TestUser1", "password", "test@test.com");

            userDAO.create(newUser1);

            userDAO.delete(newUser1.username());

            Collection<UserData> users = userDAO.getAll();

            assertTrue(users.isEmpty(), "delete() should remove created record");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("delete(): Delete Non-existent")
    void testDeleteNonExistent(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            assertThrows(ItemNotFoundException.class, () -> userDAO.delete("TestUser1"), "Should throw ItemNotFoundException");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("update(): Successful Update")
    void testUpdateRecord(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData newUser1 = new UserData("TestUser1", "password", "test@test.com");

            userDAO.create(newUser1);

            UserData updatedUser = new UserData("TestUser1", "newPassword", "new@test.com");

            userDAO.update(updatedUser);

            UserData user = userDAO.get("TestUser1");

            assertTrue(user.equals(updatedUser), "update() should change existing record");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("update(): Update Non-existent")
    void testUpdateNonExistent(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData fakeUser = new UserData("FakeUser", "password", "fake@test.com");

            assertThrows(ItemNotFoundException.class, () -> userDAO.update(fakeUser), "Should throw ItemNotFoundException");

        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("get(): Get Successfully")
    void testGetSuccessfully(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData newUser = new UserData("TestUser1", "password", "test@test.com");
            userDAO.create(newUser);

            UserData user = userDAO.get("TestUser1");

            assertTrue(newUser.equals(user), "Should return same object");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("get(): Get Non-existent")
    void testGetNonExistent(){

        try{
            DataAccessObject<UserData> userDAO = new SQLUserDAO();

            UserData user = userDAO.get("TestUser1");

            assertNull(user, "Should return null");
        }
        catch (Exception e){
            fail("Should not throw exception: " + e.getMessage());
        }

    }
}
