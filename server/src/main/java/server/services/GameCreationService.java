package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.GameData;
import server.DataFactory;

public class GameCreationService extends Service{

    public GameData createGame (String gameName, DataAccessObject<GameData> gameDAO, DataFactory<GameData> gameFactory) throws ServiceException{

        try {
            GameData newGame = gameFactory.createData(gameName);

            gameDAO.create(newGame);

            return newGame;

        }
        catch (Exception e){
            throw new ServiceException("Could not create game: " + e.getMessage());
        }
    }

}
