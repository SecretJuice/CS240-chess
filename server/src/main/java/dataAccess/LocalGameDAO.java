package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class LocalGameDAO implements GameDataAccess{

    private static HashMap<Integer, GameData> games = new HashMap<>();

    public void createGame(GameData gameData) throws DataAccessException {

        if (games.containsKey(gameData.gameID())){
            throw new DataAccessException(String.format("Game with ID: {%s} already exists", gameData.gameID()));
        }

        games.put(gameData.gameID(), gameData);

    }

    public Collection<GameData> getAllGames() throws DataAccessException {

        return games.values();
    }

    public void updateGame(GameData gameData) throws DataAccessException {

        if (!games.containsKey(gameData.gameID())){
            throw new DataAccessException(String.format("Game with ID: {%s} was not found", gameData.gameID()));
        }

        games.replace(gameData.gameID(), gameData);

    }

    public void deleteGame(int gameID) throws DataAccessException {

        if (!games.containsKey(gameID)){
            throw new DataAccessException(String.format("Game with ID: {%s} was not found", gameID));
        }

        games.remove(gameID);

    }

    public void clearGames() throws DataAccessException {

        games.clear();

    }
}
