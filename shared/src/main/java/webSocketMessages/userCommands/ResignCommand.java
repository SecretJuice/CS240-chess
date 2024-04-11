package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand{
    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        super.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
    private int gameID;
    public int getGameID(){
        return this.gameID;
    }
}