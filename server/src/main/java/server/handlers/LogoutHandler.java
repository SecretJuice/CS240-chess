package server.handlers;

import dataAccess.DataAccessObject;
import dataAccess.LocalAuthDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.services.UserLogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler{

    private final DataAccessObject<AuthData> authDAO;

    public LogoutHandler(DataAccessObject<AuthData> authDAO){
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        AuthData session = authenticate(request, authDAO);

        UserLogoutService service = new UserLogoutService(authDAO);

        service.logoutUser(session);

        response.status(200);
        return "";

    }


}
