package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand{
    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }
    protected CommandType commandType = CommandType.RESIGN;
    private int gameID;
    public int getGameID(){
        return this.gameID;
    }
}