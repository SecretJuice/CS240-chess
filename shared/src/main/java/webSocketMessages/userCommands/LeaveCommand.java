package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand{
    public LeaveCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    protected CommandType commandType = CommandType.LEAVE;
    private int gameID;
    public int getGameID(){
        return this.gameID;
    }
}
