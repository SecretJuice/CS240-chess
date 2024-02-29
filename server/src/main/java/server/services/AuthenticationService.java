package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.AuthData;
import server.requests.UnauthorizedException;

public class AuthenticationService extends Service{

    private final DataAccessObject<AuthData> authDAO;

    public AuthenticationService (DataAccessObject<AuthData> authDataAccess){
        authDAO = authDataAccess;
    }

    public AuthData authenticateSession(String authToken) throws Exception{

        AuthData session = authDAO.get(authToken);

        if (session != null){
            return session;
        }
        else {
            throw new UnauthorizedException("Session not found");
        }

    }

}
