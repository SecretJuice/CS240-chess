package server.services;

import dataAccess.*;

public class ApplicationClearService {

    public void clearApplication(UserDataAccess userDAO, AuthDataAccess authDAO, GameDataAccess gameDAO) throws DataAccessException {

        userDAO.clearUsers();
        authDAO.clearAuths();
        gameDAO.clearGames();

    }

}
