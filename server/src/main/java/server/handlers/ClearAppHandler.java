package server.handlers;

import dataAccess.DataAccessException;
import dataAccess.LocalAuthDAO;
import dataAccess.LocalGameDAO;
import dataAccess.LocalUserDAO;
import server.services.ApplicationClearService;

public class ClearAppHandler extends Handler{

    @Override
    public String handleRequest() throws DataAccessException {
        System.out.println("Clearing Application");

        new ApplicationClearService().clearApplication(new LocalUserDAO(), new LocalAuthDAO(), new LocalGameDAO());

        String yourmom = "{\"response\": \"Cleared App\"}";

        return yourmom;
    }
}
