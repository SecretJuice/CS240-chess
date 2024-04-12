package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import web.ServerFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import websockets.ServerMessageHandler;

public class GameplayUI implements ServerMessageHandler {

    private ServerFacade server;
    private Client client;

    public GameplayUI(Client client) {
        this.client = client;
        this.server = client.server();
    }

    UIUtils ui = new UIUtils();

    boolean inGame = false;
    int gameID;

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;

    public void setTeamColor(ChessGame.TeamColor color) {
        this.teamColor = color;
    }

    private void updateBoard(LoadGameMessage message) {

        inGame = true;
        client.setJoinedGame(updateGame(client.getJoinedGame(), message.getGame()));

        BoardPainter painter = new BoardPainter();

        painter.paintBoard(message.getGame().getBoard(), teamColor);
    }

    private GameData updateGame (GameData original, ChessGame game){
        return new GameData(original.gameID(), original.whiteUsername(), original.blackUsername(), original.gameName(), game);
    }

    public void receiveMessage(String json) {

        ServerMessage message = new Gson().fromJson(json, ServerMessage.class);

        switch (message.getServerMessageType()) {
            case LOAD_GAME -> updateBoard(new Gson().fromJson(json, LoadGameMessage.class));
            case ERROR -> ui.printError(new Gson().fromJson(json, ErrorMessage.class).getMessage() + "\n");
            case NOTIFICATION ->
                    ui.printNormal(new Gson().fromJson(json, NotificationMessage.class).getMessage() + "\n");
        }
    }

}