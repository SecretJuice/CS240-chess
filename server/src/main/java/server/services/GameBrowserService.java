package server.services;

import dataAccess.DataAccessObject;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameBrowserService extends Service{

    public Collection<GameData> getGameList(DataAccessObject<GameData> gameDAO) throws ServiceException{

        try{
            Collection<GameData> games = new ArrayList<>(gameDAO.getAll());

            if (games.isEmpty()){
                throw new ServiceException("No games exist at this time");
            }
            else{
                return games;
            }
        }
        catch (Exception e){
            throw new ServiceException("Could not get games: " + e.getMessage());
        }

    }

}
