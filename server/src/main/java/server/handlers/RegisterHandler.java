package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.AuthFactoryRandomToken;
import server.requests.BadRequestException;
import server.requests.RegisterUserRequest;
import server.services.UserRegistrationService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler{

    private final DataAccessObject<UserData> userDAO;
    private final DataAccessObject<AuthData> authDAO;

    public RegisterHandler(DataAccessObject<UserData> userDAO, DataAccessObject<AuthData> authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        RegisterUserRequest registerRequest = parseRequest(request.body());

        UserRegistrationService service = new UserRegistrationService(userDAO, authDAO, new AuthFactoryRandomToken());

        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());

        AuthData newAuth = service.registerUser(newUser);

        return encodeAuthData(newAuth);
    }

    private String encodeAuthData(AuthData data) throws Exception{

        try{

            Gson parser = new Gson();
            return parser.toJson(data);

        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    private RegisterUserRequest parseRequest(String json) throws BadRequestException {

        try{

            Gson parser = new Gson();
            RegisterUserRequest request = parser.fromJson(json, RegisterUserRequest.class);

            if (request.username() == null){
                throw new BadRequestException("Missing Username");
            }
            else if (request.password() == null){
                throw new BadRequestException("Missing Password");
            }
            else if (request.email() == null){
                throw new BadRequestException("Missing Email");
            }

            return request;

        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

    }
}
