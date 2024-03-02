package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.GameData;
import data.responses.ListGameReponse;
import server.services.GameBrowserService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGameHandler extends Handler{

    private final DataAccessObject<AuthData> authDAO;
    private final DataAccessObject<GameData> gameDAO;

    public ListGameHandler(DataAccessObject<AuthData> authDAO, DataAccessObject<GameData> gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        AuthData session = authenticate(request, authDAO);

        GameBrowserService service = new GameBrowserService(gameDAO);

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
