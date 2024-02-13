package server.handlers;

import dataAccess.LocalAuthDAO;
import model.AuthData;
import server.services.UserLogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler{

    @Override
    public Object handle(Request request, Response response) throws Exception {

        AuthData session = authenticate(request);

        UserLogoutService service = new UserLogoutService(new LocalAuthDAO());

        service.logoutUser(session);

        response.status(200);
        return "";

    }


}
