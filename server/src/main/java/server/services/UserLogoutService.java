package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;

public class UserLogoutService extends Service{

    private final DataAccessObject<AuthData> authDOA;

    public UserLogoutService (DataAccessObject<AuthData> authDataAccess){
        authDOA = authDataAccess;
    }

    public void logoutUser (AuthData authData) throws ServiceException{

        try{

            authDOA.delete(authData.authToken());

        }
        catch (Exception e){
            throw new ServiceException("Failed to logout: " + e.getMessage());
        }
    }
}
