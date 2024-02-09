package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;

public class AuthenticationService extends Service{

    public AuthData authenticateSession(String authToken, DataAccessObject<AuthData> authDAO) throws ServiceException{

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
