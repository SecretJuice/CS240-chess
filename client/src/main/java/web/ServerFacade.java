package web;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import data.requests.*;
import data.responses.*;
import model.AuthData;
import model.GameData;
import webSocketMessages.userCommands.JoinPlayerCommand;
import websockets.ServerMessageHandler;
import websockets.WebSocketConnector;
import websockets.WebSocketException;

import java.util.Arrays;
import java.util.Collection;

public class ServerFacade {

    private final Gson parser = new Gson();
    private WebConnector connector = null;
    private WebSocketConnector webSocketConnector;
    private AuthData session;

    public ServerFacade(){
        connector = new WebConnector("http://localhost:7777");


    }
    public ServerFacade(String serverURL){
        connector = new WebConnector(serverURL);
    }

    public AuthData getSession() {
        return session;
    }

    public void setSession(AuthData session) {
        this.session = session;
    }


    public String login(String username, String password) throws Exception{

        LoginRequest loginRequest = new LoginRequest(username, password);
        String body = parser.toJson(loginRequest);

        HTTPResponse response = connector.request(WebConnector.Method.POST, WebConnector.EndPoint.SESSION, null, body);

        SessionResponse sessionResponse = parser.fromJson(response.body(), SessionResponse.class);

        AuthData newSession = new AuthData(sessionResponse.authToken(), sessionResponse.username());
        setSession(newSession);

        return newSession.username();
    }

    /*
    return String username
     */
    public String register(String username, String password, String email) throws Exception{

        RegisterUserRequest registrationRequest = new RegisterUserRequest(username, password, email);
        String body = parser.toJson(registrationRequest);

        HTTPResponse response = connector.request(WebConnector.Method.POST, WebConnector.EndPoint.USER, null, body);

        SessionResponse sessionResponse = parser.fromJson(response.body(), SessionResponse.class);

        AuthData newSession = new AuthData(sessionResponse.authToken(), sessionResponse.username());
        setSession(newSession);

        return newSession.username();
    }

    public void logout() throws Exception{

        if(session == null){
            throw new UnauthorizedException("Not logged in.");
        }

        HTTPResponse response = connector.request(WebConnector.Method.DELETE, WebConnector.EndPoint.SESSION, session.authToken(), null);

        setSession(null);

    }

    public int createGame(String gameName) throws Exception{

        if(session == null){
            throw new UnauthorizedException("Not logged in.");
        }

        CreateGameRequest gameRequest = new CreateGameRequest(gameName);
        String requestBody = parser.toJson(gameRequest);

        HTTPResponse response = connector.request(WebConnector.Method.POST, WebConnector.EndPoint.GAME, session.authToken(), requestBody);

        CreateGameResponse createGameResponse = parser.fromJson(response.body(), CreateGameResponse.class);

        return createGameResponse.gameID();

    }

    public Collection<GameData> listGames() throws Exception{
        if(session == null){
            throw new UnauthorizedException("Not logged in.");
        }

        HTTPResponse response = connector.request(WebConnector.Method.GET, WebConnector.EndPoint.GAME, session.authToken(), null);

        ListGameReponse listGameReponse = parser.fromJson(response.body(), ListGameReponse.class);

        return Arrays.asList(listGameReponse.games());
    }

    public void joinGame(Integer gameID, ChessGame.TeamColor color) throws Exception{

        if(session == null){
            throw new UnauthorizedException("Not logged in.");
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest(gameID, color);
        String requestBody = parser.toJson(joinGameRequest);

        HTTPResponse response = connector.request(WebConnector.Method.PUT, WebConnector.EndPoint.GAME, session.authToken(), requestBody);

        JoinPlayerCommand webSocketCommand = new JoinPlayerCommand(this.session.authToken(), gameID, color);

        initializeWebSocketConnection();

        this.webSocketConnector.joinPlayer(webSocketCommand);
    }

    private void initializeWebSocketConnection() throws WebSocketException{
        if(this.webSocketConnector == null){
            return;
        }

        String url = "http://localhost:7777";

        if (this.connector != null){
            url = this.connector.getUrl();
        }

        this.webSocketConnector = new WebSocketConnector(url,  new ServerMessageHandler());
    }


}
