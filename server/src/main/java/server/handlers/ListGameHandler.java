package server.handlers;

import com.google.gson.Gson;
import dataAccess.LocalGameDAO;
import model.AuthData;
import model.GameData;
import server.responses.ListGameReponse;
import server.services.GameBrowserService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGameHandler extends Handler{

    @Override
    public Object handle(Request request, Response response) throws Exception {

        AuthData session = authenticate(request);

        GameBrowserService service = new GameBrowserService(new LocalGameDAO());

        Collection<GameData> gameList = service.getGameList();

        ListGameReponse listGameReponse = new ListGameReponse(gameList.toArray(new GameData[0]));

        String handlerResponse = encodeResponse(listGameReponse);

        response.status(200);

        return handlerResponse;

    }

    private String encodeResponse(ListGameReponse data) throws Exception{

        try{

            Gson parser = new Gson();
            return parser.toJson(data);

        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
}
