package server.services;

import dataAccess.DataAccessObject;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameBrowserService extends Service{

    private final DataAccessObject<GameData> gameDAO;

    public GameBrowserService (DataAccessObject<GameData> gameDataAccess){
        gameDAO = gameDataAccess;
    }

    public Collection<GameData> getGameList() throws Exception{

        try{
            Collection<GameData> games = new ArrayList<>(gameDAO.getAll());

//            if (games.isEmpty()){
//                throw new ServiceException("No games exist at this time");
//            }
//            else{
//                return games;
//            }

            return games;
        }
        catch (Exception e){
            throw new Exception("Could not get games: " + e.getMessage());
        }

    }

}
