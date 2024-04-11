package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessObject;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.services.AuthenticationService;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.userCommands.*;

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


        switch (command.getCommandType()){
            case JOIN_PLAYER -> joinPlayer((JoinPlayerCommand) command, auth, session);
            case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) command);
            case LEAVE -> leave((LeaveCommand) command);
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command);
            case RESIGN -> resign((ResignCommand) command);
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

    private void joinPlayer(JoinPlayerCommand command, AuthData authData, Session session) throws Exception{
        WebSocketConnection connection = connectionManager.add(authData, session);

        GameData gameData = gameDAO.get(Integer.toString(command.getGameID()));

        ChessGame game = gameData.game();

        LoadGameMessage loadGameMessage = new LoadGameMessage(game);

        connection.send(new Gson().toJson(loadGameMessage));
        connectionManager.broadcast(connection.auth.authToken(), authData.username() + " has joined team " + command.getPlayerColor().toString());
    }

    private void joinObserver(JoinObserverCommand command) throws Exception{
        throw new RuntimeException("JOIN_OBSERVER Not implemented");
    }

    private void leave(LeaveCommand command) throws Exception{
        throw new RuntimeException("LEAVE Not implemented");
    }

    private void makeMove(MakeMoveCommand command) throws Exception{
        throw new RuntimeException("MAKE_MOVE Not implemented");
    }

    private void resign(ResignCommand command) throws Exception{
        throw new RuntimeException("RESIGN Not implemented");
    }

}
