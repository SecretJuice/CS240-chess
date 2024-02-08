package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.AuthFactory;

public class UserRegistrationService extends Service{

    public AuthData registerUser(UserData userData, DataAccessObject<UserData> userDAO, DataAccessObject<AuthData> authDAO, AuthFactory authFactory) throws ServiceException{

        try {
            userDAO.create(userData);

            return createSession(userData, authDAO, authFactory);
        }
        catch (DataAccessException e){
            throw new ServiceException(e.getMessage());
        }

    }

}
