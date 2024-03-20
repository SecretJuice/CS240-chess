package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;
import model.GameData;
import data.requests.BadRequestException;
import data.requests.ForbiddenException;
import data.requests.JoinGameRequest;
import data.requests.UnauthorizedException;

import java.util.Objects;

public class JoinGameService extends Service{

    private final DataAccessObject<GameData> gameDAO;

    public JoinGameService(DataAccessObject<GameData> gameDataAccess){

        gameDAO = gameDataAccess;

    }

    public GameData joinGame(JoinGameRequest request, AuthData session) throws Exception{

        //Verify that we have a session/username
        if (session == null){
            throw new UnauthorizedException("No session");
        }

        GameData joinedGame = gameDAO.get(Integer.toString(request.gameID()));

        //Verify game exists
        if (joinedGame == null){
            throw new BadRequestException(String.format("Game with ID: %s does not exist", request.gameID()));
        }


        GameData newGame = null;

        if (request.playerColor() != null){

            //Verify desired team isn't taken
            switch (request.playerColor()){
                case WHITE -> {
                    if ((!Objects.equals(joinedGame.whiteUsername(), session.username())) && joinedGame.whiteUsername() != null){
                        throw new ForbiddenException("Another user has already joined WHITE team");
                    }
                }
                case BLACK -> {
                    if ((!Objects.equals(joinedGame.blackUsername(), session.username())) && joinedGame.blackUsername() != null) {
                        throw new ForbiddenException("Another user has already joined BLACK team");
                    }
                }
            }

            switch (request.playerColor()){
                case WHITE -> newGame = new GameData(joinedGame.gameID(), session.username(), joinedGame.blackUsername(), joinedGame.gameName(), joinedGame.game());
                case BLACK -> newGame = new GameData(joinedGame.gameID(), joinedGame.whiteUsername(), session.username(), joinedGame.gameName(), joinedGame.game());
            }
        }
        else{
            newGame = joinedGame;
        }


        gameDAO.update(newGame);

        return newGame;

    }

}
