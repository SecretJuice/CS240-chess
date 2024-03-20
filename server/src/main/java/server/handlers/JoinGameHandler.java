package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.GameData;
import data.requests.BadRequestException;
import data.requests.JoinGameRequest;
import server.services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{

    private final DataAccessObject<AuthData> authDAO;
    private final DataAccessObject<GameData> gameDAO;

    public JoinGameHandler(DataAccessObject<AuthData> authDAO, DataAccessObject<GameData> gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        AuthData session = authenticate(request, authDAO);

        JoinGameRequest joinGameRequest = parseRequest(request.body());

        JoinGameService service = new JoinGameService(gameDAO);

        service.joinGame(joinGameRequest, session);

        response.status(200);

        return "";

    }

    private JoinGameRequest parseRequest(String json) throws BadRequestException {

        try{

            Gson parser = new Gson();
            JoinGameRequest request = parser.fromJson(json, JoinGameRequest.class);

            Integer gameID = request.gameID();

            if (gameID == null){
                throw new BadRequestException("Missing gameName");
            }

            return request;

        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

    }

}
