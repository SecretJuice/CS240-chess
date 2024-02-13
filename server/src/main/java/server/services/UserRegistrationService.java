package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.ItemAlreadyExistsException;
import model.AuthData;
import model.UserData;
import server.DataFactory;
import server.requests.ForbiddenException;

public class UserRegistrationService extends Service{

    private final DataAccessObject<UserData> userDAO;
    private final DataAccessObject<AuthData> authDAO;
    private final DataFactory<AuthData> authFactory;

    public UserRegistrationService (DataAccessObject<UserData> userDataAccess, DataAccessObject<AuthData> authDataAccess, DataFactory<AuthData> authDataFactory){

        userDAO = userDataAccess;
        authDAO = authDataAccess;
        authFactory = authDataFactory;

    }

    public AuthData registerUser(UserData userData) throws Exception{

        try{
            userDAO.create(userData);
        }
        catch (ItemAlreadyExistsException e){
            throw new ForbiddenException("Username already taken");
        }

        return createSession(userData, authDAO, authFactory);

    }

}
