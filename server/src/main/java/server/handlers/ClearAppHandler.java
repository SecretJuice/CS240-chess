package server.handlers;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.services.ApplicationClearService;
import spark.Request;
import spark.Response;

public class ClearAppHandler extends Handler{

    private final DataAccessObject<UserData> userDAO;
    private final DataAccessObject<AuthData> authDAO;
    private final DataAccessObject<GameData> gameDAO;

    public ClearAppHandler(DataAccessObject<UserData> userDAO, DataAccessObject<AuthData> authDAO, DataAccessObject<GameData> gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        ApplicationClearService service = new ApplicationClearService(userDAO, authDAO, gameDAO);

        service.clearApplication();

        response.status(200);
        return "";


    }
}
