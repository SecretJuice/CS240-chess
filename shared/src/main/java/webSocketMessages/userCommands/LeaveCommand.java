package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand{
    public LeaveCommand(String authToken, Integer gameID) {
        super(authToken, gameID);
        super.commandType = CommandType.LEAVE;
    }
}
