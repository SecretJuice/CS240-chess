package server.handlers;

import dataAccess.DataAccessException;
import dataAccess.LocalAuthDAO;
import dataAccess.LocalGameDAO;
import dataAccess.LocalUserDAO;
import server.services.ApplicationClearService;
import spark.Request;
import spark.Response;

public class ClearAppHandler extends Handler{


    @Override
    public Object handle(Request request, Response response) throws Exception {

        System.out.println("ClearAppHandler called");

        ApplicationClearService service = new ApplicationClearService(new LocalUserDAO(), new LocalAuthDAO(), new LocalGameDAO());

        service.clearApplication();

        response.status(200);
        return "";


    }
}
