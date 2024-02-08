package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    public void createGame(GameData gameData) throws DataAccessException;
    public void updateGame(GameData gameData) throws DataAccessException;
    public void deleteGame(int gameID) throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public Collection<GameData> getAllGames() throws DataAccessException;
}
