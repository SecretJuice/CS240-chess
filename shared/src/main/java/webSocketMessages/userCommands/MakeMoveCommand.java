package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    public MakeMoveCommand(String authToken, Integer gameID, ChessMove chessMove) {
        super(authToken, gameID);
        super.commandType = CommandType.MAKE_MOVE;
        this.move = chessMove;
    }

    public ChessMove getMove() {
        return move;
    }
    private ChessMove move;
}
