package websockets;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketConnector extends Endpoint {

    ServerMessageHandler messageHandler;

    Session session;

    public WebSocketConnector(String url, ServerMessageHandler messageHandler) throws WebSocketException {
        this.messageHandler = messageHandler;
        createConnection(url);
    }

    private void createConnection(String url) throws WebSocketException{
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                messageHandler.notify(serverMessage);

            });
        }
        catch(DeploymentException | IOException | URISyntaxException e) {
            throw new WebSocketException(500, e.getMessage());
        }
    }

    public void joinPlayer(JoinPlayerCommand command) throws WebSocketException{
        String message = new Gson().toJson(command);
        try{
            this.session.getBasicRemote().sendText(message);
        }
        catch(IOException e){
            throw new WebSocketException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
