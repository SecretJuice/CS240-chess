package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{

    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken, gameID);
        super.commandType = CommandType.JOIN_OBSERVER;
    }



}
