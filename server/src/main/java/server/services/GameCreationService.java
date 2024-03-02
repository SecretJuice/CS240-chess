package server.services;

import dataAccess.DataAccessObject;
import dataAccess.ItemAlreadyExistsException;
import model.GameData;
import server.DataFactory;
import data.requests.BadRequestException;

public class GameCreationService extends Service{

    private final DataAccessObject<GameData> gameDAO;
    private final DataFactory<GameData> gameFactory;

    public GameCreationService (DataAccessObject<GameData> gameDataAccess, DataFactory<GameData> gameDataFactory){
        gameDAO = gameDataAccess;
        gameFactory = gameDataFactory;
    }


    public GameData createGame (String gameName) throws Exception{

            GameData newGame = gameFactory.createData(gameName);

            try {
                gameDAO.create(newGame);
            }
            catch (ItemAlreadyExistsException e){
                throw new BadRequestException("Could not create game: " + e.getMessage());
            }

            return newGame;

    }

}
