package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.DataFactory;

public class UserRegistrationService extends Service{

    private final DataAccessObject<UserData> userDAO;
    private final DataAccessObject<AuthData> authDAO;
    private final DataFactory<AuthData> authFactory;

    public UserRegistrationService (DataAccessObject<UserData> userDataAccess, DataAccessObject<AuthData> authDataAccess, DataFactory<AuthData> authDataFactory){

        userDAO = userDataAccess;
        authDAO = authDataAccess;
        authFactory = authDataFactory;

    }

    public AuthData registerUser(UserData userData) throws ServiceException{

        try {
            userDAO.create(userData);

            return createSession(userData, authDAO, authFactory);
        }
        catch (DataAccessException e){
            throw new ServiceException(e.getMessage());
        }

    }

}
