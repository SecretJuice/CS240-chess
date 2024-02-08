package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LocalUserDAO implements UserDataAccess{

    private static HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {

        return users.get(username);
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {

        if (users.containsValue(userData)){
            throw new DataAccessException("User already exists");
        }

        users.put(userData.username(), userData);
    }

    @Override
    public void updateUser(UserData userData) throws DataAccessException {

        if (!users.containsKey(userData.username())){
            throw new DataAccessException("User not found");
        }

        users.replace(userData.username(), userData);
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        if (!users.containsKey(username)){
            throw new DataAccessException("User not found");
        }

        users.remove(username);
    }

    @Override
    public void clearUsers() throws DataAccessException {

        users.clear();

    }

    public Collection<UserData> getAllUsers() throws DataAccessException {
        return users.values();
    }
}
