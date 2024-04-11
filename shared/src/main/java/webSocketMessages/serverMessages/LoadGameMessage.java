package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    public ChessGame getGame() {
        return game;
    }

    private ChessGame game;

    public LoadGameMessage(ChessGame game) {
        this.game = game;
        this.serverMessageType = ServerMessageType.LOAD_GAME;
    }
}
