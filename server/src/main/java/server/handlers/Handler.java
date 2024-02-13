package server.handlers;

import dataAccess.DataAccessException;
import dataAccess.LocalAuthDAO;
import model.AuthData;
import server.services.AuthenticationService;
import spark.Request;
import spark.Response;
import spark.Route;

public class Handler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception{
        throw new Exception("Not Implemented");
    }

    protected AuthData authenticate(Request req) throws Exception{
        AuthenticationService authService = new AuthenticationService(new LocalAuthDAO());

        return authService.authenticateSession(req.headers("Authorization"));
    }


}
