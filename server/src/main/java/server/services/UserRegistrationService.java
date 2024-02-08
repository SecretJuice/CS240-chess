package server.services;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import server.AuthFactory;

import java.util.UUID;

public class UserRegistrationService {

    public AuthData registerUser(UserData userData, UserDataAccess userDAO, AuthDataAccess authDAO, AuthFactory authFactory) throws ServiceException{

        try {
            userDAO.createUser(userData);

            AuthData authData = authFactory.createAuthData(userData.username());

            authDAO.createAuth(authData);

            return authData;
        }
        catch (DataAccessException e){
            throw new ServiceException(e.getMessage());
        }

    }

}
