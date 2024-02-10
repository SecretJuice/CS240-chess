package server.services;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.GameData;
import server.DataFactory;

public class GameCreationService extends Service{

    private final DataAccessObject<GameData> gameDAO;
    private final DataFactory<GameData> gameFactory;

    public GameCreationService (DataAccessObject<GameData> gameDataAccess, DataFactory<GameData> gameDataFactory){
        gameDAO = gameDataAccess;
        gameFactory = gameDataFactory;
    }


    public GameData createGame (String gameName) throws ServiceException{

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
