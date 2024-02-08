package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDataAccess {
    public void createAuth(AuthData authData) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(AuthData authData) throws DataAccessException;
    public void clearAuths() throws DataAccessException;
    public Collection<AuthData> getAllAuths() throws DataAccessException;
}
