package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import web.ServerFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import websockets.ServerMessageHandler;

public class GameplayUI implements ServerMessageHandler {

    private ServerFacade server;

    public GameplayUI(ServerFacade server) {
        this.server = server;
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

        BoardPainter painter = new BoardPainter();

        painter.paintBoard(message.getGame().getBoard(), teamColor);
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


    private void processGameplayCommand(String[] args) {

        try{

            switch (args[0].toLowerCase().trim()) {
                case "leave" -> leaveGame();
            }
        }
        catch(Exception e){
            ui.printError(e.getMessage() + "\n");
        }
    }

    private void leaveGame() throws Exception {
        server.leaveGame(gameID);
        inGame = false;
    }
}