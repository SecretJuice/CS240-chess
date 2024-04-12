package ui;

import model.GameData;
import web.ServerFacade;

import java.util.HashMap;

public class Client {

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
