package server.handlers;

import com.google.gson.Gson;
import dataAccess.LocalGameDAO;
import model.AuthData;
import server.requests.BadRequestException;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import server.services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{

    @Override
    public Object handle(Request request, Response response) throws Exception {

        AuthData session = authenticate(request);

        JoinGameRequest joinGameRequest = parseRequest(request.body());

        JoinGameService service = new JoinGameService(new LocalGameDAO());

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
