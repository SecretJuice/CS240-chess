package ui;

import model.GameData;
import web.ServerFacade;

import java.util.HashMap;

public class Client {

    public enum State{
        LOGGEDOUT,
        LOGGEDIN,
        INGAME
    }

    private State state = State.LOGGEDOUT;
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }

    public GameData getJoinedGame() {
        return joinedGame;
    }

    public void setJoinedGame(GameData joinedGame) {
        this.joinedGame = joinedGame;
    }

    private GameData joinedGame;

    private UserInterface ui = new UserInterface(this);

    public UserInterface ui(){
        return this.ui;
    }

    private ServerFacade server = null;

    public ServerFacade server(){
        return this.server;
    }

    private CommandProcessor commandProcessor = new CommandProcessor(this);

    public CommandProcessor commands(){
        return commandProcessor;
    }

    private final HashMap<Integer, GameData> savedGameMap = new HashMap<>();

    public HashMap<Integer, GameData> savedGames(){
        return savedGameMap;
    }

    public Client(ServerFacade serverFacade){
        server = serverFacade;
    }

    public boolean isLoggedIn(){

        if (server != null){

            return server.getSession() != null;
        }
        return false;
    }

}
