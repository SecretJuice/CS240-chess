package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
    public UserData getUser(String username) throws DataAccessException;
    public void createUser(UserData userData) throws DataAccessException;
    public void updateUser(UserData userData) throws DataAccessException;
    public void deleteUser(String username) throws DataAccessException;
    public void clearUsers() throws DataAccessException;
    public Collection<UserData> getAllUsers() throws DataAccessException;
}
