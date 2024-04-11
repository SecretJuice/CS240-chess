package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import data.requests.BadRequestException;
import data.requests.ForbiddenException;
import data.requests.UnauthorizedException;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.services.AuthenticationService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.userCommands.*;

import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private AuthenticationService authService;
    private DataAccessObject<GameData> gameDAO;
    private WebSocketConnectionManager connectionManager = new WebSocketConnectionManager();

    public WebSocketHandler(AuthenticationService authService, DataAccessObject<GameData> gameDAO){
        this.authService = authService;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{
        UserGameCommand command = parseMessage((message));

        AuthData auth = authService.authenticateSession(command.getAuthString());
        WebSocketConnection connection = connectionManager.add(auth, session);

        try{
            if (auth == null){
                throw new UnauthorizedException("Invalid Session");
            }
            switch (command.getCommandType()){
                case JOIN_PLAYER -> joinPlayer((JoinPlayerCommand) command, connection);
                case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) command, connection);
                case LEAVE -> leave((LeaveCommand) command);
                case MAKE_MOVE -> makeMove((MakeMoveCommand) command, connection);
                case RESIGN -> resign((ResignCommand) command);
            }
        }
        catch(Exception e){
            ErrorMessage errorMessage = new ErrorMessage("Error: " + e.getMessage());
            System.out.println(errorMessage.getMessage());
            connection.send(new Gson().toJson(errorMessage));
        }

    }

    private UserGameCommand parseMessage(String message) throws Exception{
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);


        return switch (command.getCommandType()) {
            case JOIN_PLAYER -> new Gson().fromJson(message, JoinPlayerCommand.class);
            case JOIN_OBSERVER -> new Gson().fromJson(message, JoinObserverCommand.class);
            case LEAVE -> new Gson().fromJson(message, LeaveCommand.class);
            case MAKE_MOVE -> new Gson().fromJson(message, MakeMoveCommand.class);
            case RESIGN -> new Gson().fromJson(message, ResignCommand.class);
        };
    }

    private void joinPlayer(JoinPlayerCommand command, WebSocketConnection connection) throws Exception{
        GameData gameData = gameDAO.get(Integer.toString(command.getGameID()));

        if (gameData == null){
            throw new BadRequestException("Game with ID " + command.getGameID() + " does not exist");
        }

        if (command.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(gameData.whiteUsername(), connection.auth.username()) ||
           (command.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(gameData.blackUsername(), connection.auth.username()))){

            throw new ForbiddenException("User not joined under specified color " + command.getPlayerColor());
        }

        ChessGame game = gameData.game();
        LoadGameMessage loadGameMessage = new LoadGameMessage(game);

        connection.send(new Gson().toJson(loadGameMessage));
        connectionManager.broadcast(connection.auth.authToken(), connection.auth.username() + " has joined team " + command.getPlayerColor().toString());
    }

    private void joinObserver(JoinObserverCommand command, WebSocketConnection connection) throws Exception{
        GameData gameData = gameDAO.get(Integer.toString(command.getGameID()));

        if (gameData == null){
            throw new BadRequestException("Game with ID " + command.getGameID() + " does not exist");
        }

        ChessGame game = gameData.game();
        LoadGameMessage loadGameMessage = new LoadGameMessage(game);

        connection.send(new Gson().toJson(loadGameMessage));
        connectionManager.broadcast(connection.auth.authToken(), connection.auth.username() + " is now spectating");
    }

    private void leave(LeaveCommand command) throws Exception{
        throw new RuntimeException("LEAVE Not implemented");
    }

    private void makeMove(MakeMoveCommand command, WebSocketConnection connection) throws Exception{

        GameData gameData = gameDAO.get(Integer.toString(command.getGameID()));

        if (gameData == null){
            throw new BadRequestException("Game with ID " + command.getGameID() + " does not exist");
        }

        ChessGame game = gameData.game();
        String username = connection.auth.username();

        if (game.getTeamTurn() != getPlayerTeam(username, gameData)){

            if (getPlayerTeam(username, gameData) == null){
                throw new ForbiddenException("User is not playing in this game.");
            }

            throw new Exception("Playing out of turn.");
        }

        game.makeMove(command.getMove());

        LoadGameMessage loadGameMessage = new LoadGameMessage(game);
        connectionManager.syncGame(loadGameMessage);
        connectionManager.broadcast(connection.auth.authToken(), username + " moved from " + command.getMove().getStartPosition().toString()
                                                                                    + " to " + command.getMove().getEndPosition().toString());
    }

    private ChessGame.TeamColor getPlayerTeam(String username, GameData gameData){
        if (Objects.equals(username, gameData.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(username, gameData.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        }

        return null;
    }

    private void resign(ResignCommand command) throws Exception{
        throw new RuntimeException("RESIGN Not implemented");
    }

}
