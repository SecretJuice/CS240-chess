package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class LocalAuthDAO implements AuthDataAccess{

    private static HashMap<String, AuthData> sessions = new HashMap<>();

    public void createAuth(AuthData authData) throws DataAccessException {
        if (sessions.containsKey(authData.authToken())){
            throw new DataAccessException("Auth token already exists");
        }

        sessions.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return sessions.get(authToken);
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        if (!sessions.containsKey(authData.authToken())){
            throw new DataAccessException("Auth token does not exist");
        }

        sessions.remove(authData.authToken());
    }

    public void clearAuths() throws DataAccessException {
        sessions.clear();
    }

    @Override
    public Collection<AuthData> getAllAuths() throws DataAccessException {
        return sessions.values();
    }
}
