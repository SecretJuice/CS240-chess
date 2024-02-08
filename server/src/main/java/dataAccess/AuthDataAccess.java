package dataAccess;

import model.AuthData;

public interface AuthDataAccess {
    public void createAuth(AuthData authData) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(AuthData authData) throws DataAccessException;
    public void clearAuths() throws DataAccessException;
}
