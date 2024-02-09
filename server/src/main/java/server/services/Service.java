package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.AuthFactory;

public abstract class Service {

    protected AuthData createSession(UserData userData, DataAccessObject<AuthData> authDAO, AuthFactory authFactory) throws ServiceException {

        try{

            AuthData authData = authFactory.createAuthData(userData.username());

            authDAO.create(authData);

            return authData;
        }
        catch (DataAccessException e){

            throw new ServiceException("Could not create Session");
        }
    }

}
