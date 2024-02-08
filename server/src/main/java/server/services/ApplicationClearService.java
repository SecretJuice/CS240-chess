package server.services;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

public class ApplicationClearService {

    public void clearApplication(DataAccessObject<UserData> userDAO, DataAccessObject<AuthData> authDAO, DataAccessObject<GameData> gameDAO) throws DataAccessException {

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

    }

}
