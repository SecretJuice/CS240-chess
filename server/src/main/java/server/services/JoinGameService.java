package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;
import model.GameData;
import server.requests.JoinGameRequest;

import java.util.Objects;

public class JoinGameService extends Service{

    public GameData joinGame(JoinGameRequest request, AuthData session, DataAccessObject<GameData> gameDAO) throws ServiceException{

        try{

            //Verify that we have a session/username
            if (session == null){
                throw new ServiceException("No session");
            }

            GameData joinedGame = gameDAO.get(Integer.toString(request.gameID()));

            //Verify game exists
            if (joinedGame == null){
                throw new ServiceException(String.format("Game with ID: %s does not exist", request.gameID()));
            }

            //Verify desired team isn't taken
            switch (request.teamColor()){
                case WHITE -> {
                    if (!Objects.equals(joinedGame.whiteUsername(), session.username())) { throw new ServiceException("Another user has already joined WHITE team"); }
                }
                case BLACK -> {
                    if (!Objects.equals(joinedGame.blackUsername(), session.username())) { throw new ServiceException("Another user has already joined BLACK team"); }
                }
            }

            GameData newGame = null;

            switch (request.teamColor()){
                case WHITE -> newGame = new GameData(joinedGame.gameID(), session.username(), joinedGame.blackUsername(), joinedGame.gameName(), joinedGame.game());
                case BLACK -> newGame = new GameData(joinedGame.gameID(), joinedGame.whiteUsername(), session.username(), joinedGame.gameName(), joinedGame.game());
            }

            gameDAO.update(newGame);

            return newGame;


        }
        catch (Exception e){
            throw new ServiceException("Could not join game: " + e.getMessage());
        }

    }

}
