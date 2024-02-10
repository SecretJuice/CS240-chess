package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;

public class AuthenticationService extends Service{

    private final DataAccessObject<AuthData> authDAO;

    public AuthenticationService (DataAccessObject<AuthData> authDataAccess){
        authDAO = authDataAccess;
    }

    public AuthData authenticateSession(String authToken) throws ServiceException{

        try {
            AuthData session = authDAO.get(authToken);

            if (session != null){
                return session;
            }
            else {
                throw new ServiceException("Session not found");
            }

        }
        catch (Exception e){
            throw new ServiceException("[Invalid Session]: " + e.getMessage());
        }

    }

}
