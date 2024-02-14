package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * DataAccessObject for storing UserData objects (users).
 * Stores objects in memory via HashMap
 */
public class LocalUserDAO implements DataAccessObject<UserData>{

    private static HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData get(String username) throws DataAccessException {

        return users.get(username);
    }

    @Override
    public void create(UserData userData) throws DataAccessException {

        if (users.containsKey(userData.username())){
            throw new ItemAlreadyExistsException("User already exists");
        }

        users.put(userData.username(), userData);
    }

    @Override
    public void update(UserData userData) throws DataAccessException {

        if (!users.containsKey(userData.username())){
            throw new DataAccessException("User not found");
        }

        users.replace(userData.username(), userData);
    }

    @Override
    public void delete(String username) throws DataAccessException {
        if (!users.containsKey(username)){
            throw new DataAccessException("User not found");
        }

        users.remove(username);
    }

    @Override
    public void clear() throws DataAccessException {

        users.clear();

    }

    public Collection<UserData> getAll() throws DataAccessException {
        return users.values();
    }
}
