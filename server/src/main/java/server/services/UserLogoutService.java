package server.services;

import dataAccess.DataAccessObject;
import dataAccess.ItemNotFoundException;
import model.AuthData;
import server.requests.UnauthorizedException;

public class UserLogoutService extends Service{

    private final DataAccessObject<AuthData> authDOA;

    public UserLogoutService (DataAccessObject<AuthData> authDataAccess){
        authDOA = authDataAccess;
    }

    public void logoutUser (AuthData authData) throws Exception{

        try{
            authDOA.delete(authData.authToken());
        }
        catch (ItemNotFoundException e){
            throw new UnauthorizedException("No session to delete: " + e.getMessage());
        }
    }
}
