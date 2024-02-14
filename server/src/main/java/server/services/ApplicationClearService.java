package server.services;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.DataFactory;

public class ApplicationClearService {

    private final DataAccessObject<UserData> userDAO;
    private final DataAccessObject<AuthData> authDAO;
    private final DataAccessObject<GameData> gameDAO;

    public ApplicationClearService (DataAccessObject<UserData> userDataAccess, DataAccessObject<AuthData> authDataAccess, DataAccessObject<GameData> gameDataAccess){
        userDAO = userDataAccess;
        authDAO = authDataAccess;
        gameDAO = gameDataAccess;
    }

    public void clearApplication() throws Exception {

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

    }

}
