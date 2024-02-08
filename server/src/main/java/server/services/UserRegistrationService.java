package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.AuthFactory;

public class UserRegistrationService {

    public AuthData registerUser(UserData userData, DataAccessObject<UserData> userDAO, DataAccessObject<AuthData> authDAO, AuthFactory authFactory) throws ServiceException{

        try {
            userDAO.create(userData);

            AuthData authData = authFactory.createAuthData(userData.username());

            authDAO.create(authData);

            return authData;
        }
        catch (DataAccessException e){
            throw new ServiceException(e.getMessage());
        }

    }

}
