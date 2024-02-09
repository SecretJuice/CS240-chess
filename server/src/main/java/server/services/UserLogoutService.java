package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;

public class UserLogoutService extends Service{

    public void logoutUser (AuthData authData, DataAccessObject<AuthData> authDOA) throws ServiceException{

        try{

            authDOA.delete(authData.authToken());

        }
        catch (Exception e){
            throw new ServiceException("Failed to logout: " + e.getMessage());
        }
    }
}
