package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessObject;
import dataAccess.LocalAuthDAO;
import dataAccess.LocalGameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.GameFactoryRandomID;
import server.requests.BadRequestException;
import server.requests.CreateGameRequest;
import server.requests.RegisterUserRequest;
import server.responses.CreateGameResponse;
import server.services.AuthenticationService;
import server.services.GameCreationService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{

    private final DataAccessObject<AuthData> authDAO;
    private final DataAccessObject<GameData> gameDAO;

    public CreateGameHandler(DataAccessObject<AuthData> authDAO, DataAccessObject<GameData> gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception{

        AuthData authData = authenticate(request, authDAO);

        CreateGameRequest createGameRequest = parseRequest(request.body());

        GameCreationService service = new GameCreationService(gameDAO, new GameFactoryRandomID());

        GameData newGame = service.createGame(createGameRequest.gameName());

        String handlerResponse = encodeRequest(new CreateGameResponse(newGame.gameID()));

        response.status(200);
        return handlerResponse;

    }

    private String encodeRequest(CreateGameResponse data) throws Exception{
        try{

            Gson parser = new Gson();
            return parser.toJson(data);

        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private CreateGameRequest parseRequest(String json) throws BadRequestException {

        try{

            Gson parser = new Gson();
            CreateGameRequest request = parser.fromJson(json, CreateGameRequest.class);

            if (request.gameName() == null){
                throw new BadRequestException("Missing gameName");
            }

            return request;

        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

    }

}
