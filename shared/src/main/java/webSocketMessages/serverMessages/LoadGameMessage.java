package webSocketMessages.serverMessages;

import chess.ChessBoard;

public class LoadGameMessage extends ServerMessage{

    public ChessBoard getGame() {
        return game;
    }

    private ChessBoard game;

    public LoadGameMessage(ChessBoard game) {
        this.game = game;
        this.serverMessageType = ServerMessageType.LOAD_GAME;
    }
}
