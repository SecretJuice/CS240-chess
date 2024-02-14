package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

/**
 * DataAccessObject for storing AuthData objects (sessions).
 * Stores objects in memory via HashMap
 */
public class LocalAuthDAO implements DataAccessObject<AuthData>{

    private static HashMap<String, AuthData> sessions = new HashMap<>();

    public void create(AuthData authData) throws DataAccessException {
        if (sessions.containsKey(authData.authToken())){
            throw new DataAccessException("Auth token already exists");
        }

        sessions.put(authData.authToken(), authData);
    }

    public AuthData get(String authToken) throws DataAccessException {
        return sessions.get(authToken);
    }

    public void update(AuthData authData) throws DataAccessException{
        if (!sessions.containsKey(authData.authToken())){
            throw new DataAccessException("Session does not exist");
        }
    }

    public void delete(String authToken) throws DataAccessException {
        if (!sessions.containsKey(authToken)){
            throw new DataAccessException("Session does not exist");
        }

        sessions.remove(authToken);
    }

    public void clear() throws DataAccessException {
        sessions.clear();
    }

    @Override
    public Collection<AuthData> getAll() throws DataAccessException {
        return sessions.values();
    }
}
