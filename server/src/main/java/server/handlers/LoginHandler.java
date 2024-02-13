package server.handlers;

import com.google.gson.Gson;
import dataAccess.LocalAuthDAO;
import dataAccess.LocalUserDAO;
import model.AuthData;
import model.UserData;
import server.AuthFactoryRandomToken;
import server.requests.BadRequestException;
import server.requests.LoginRequest;
import server.responses.SessionResponse;
import server.services.UserLoginService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler{

    @Override
    public Object handle(Request request, Response response) throws Exception {

        LoginRequest loginRequest = parseRequest(request.body());

        UserLoginService service = new UserLoginService(new LocalUserDAO(), new LocalAuthDAO(), new AuthFactoryRandomToken());

        AuthData session = service.loginUser(new UserData(loginRequest.username(), loginRequest.password(), null));

        String handlerResponse = encodeResponse(new SessionResponse(session.username(), session.authToken()));

        response.status(200);

        return handlerResponse;

    }

    private String encodeResponse(SessionResponse data) throws Exception{
        try{

            Gson parser = new Gson();
            return parser.toJson(data);

        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private LoginRequest parseRequest(String json) throws BadRequestException {

        try{

            Gson parser = new Gson();
            LoginRequest request = parser.fromJson(json, LoginRequest.class);

            if (request.username() == null){
                throw new BadRequestException("Missing username");
            }
            else if (request.password() == null){
                throw new BadRequestException("Missing password");
            }

            return request;

        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

    }
}
