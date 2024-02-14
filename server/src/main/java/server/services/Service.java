package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.DataFactory;

public abstract class Service {

    protected AuthData createSession(UserData userData, DataAccessObject<AuthData> authDAO, DataFactory<AuthData> authFactory) throws Exception {

            AuthData authData = authFactory.createData(userData.username());

            authDAO.create(authData);

            return authData;
    }

}
