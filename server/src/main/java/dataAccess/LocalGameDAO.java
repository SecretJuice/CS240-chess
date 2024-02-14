package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

/**
 * DataAccessObject for storing GameData objects (matches).
 * Stores objects in memory via HashMap
 */
public class LocalGameDAO implements DataAccessObject<GameData>{

    private static HashMap<Integer, GameData> games = new HashMap<>();

    public void create(GameData gameData) throws DataAccessException {

        if (games.containsKey(gameData.gameID())){
            throw new DataAccessException(String.format("Game with ID: {%s} already exists", gameData.gameID()));
        }

        games.put(gameData.gameID(), gameData);

    }

    public GameData get(String gameID) throws DataAccessException{
        return games.get(Integer.parseInt(gameID));
    }

    public Collection<GameData> getAll() throws DataAccessException {

        return games.values();
    }

    public void update(GameData gameData) throws DataAccessException {

        if (!games.containsKey(gameData.gameID())){
            throw new DataAccessException(String.format("Game with ID: {%s} was not found", gameData.gameID()));
        }

        games.replace(gameData.gameID(), gameData);

    }

    public void delete(String gameID) throws DataAccessException {

        if (!games.containsKey(Integer.parseInt(gameID))){
            throw new DataAccessException(String.format("Game with ID: {%s} was not found", gameID));
        }

        games.remove(gameID);

    }

    public void clear() throws DataAccessException {

        games.clear();

    }
}
