package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    public int getGameID() {
        return gameID;
    }

    private Integer gameID;

    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken);
        super.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }



}
