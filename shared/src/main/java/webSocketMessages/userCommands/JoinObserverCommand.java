package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    public int getGameID() {
        return gameID;
    }

    private int gameID;

    protected CommandType commandType = CommandType.JOIN_OBSERVER;

    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }



}
