package ui;

import model.GameData;
import web.ServerFacade;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {

    enum state{
        LOGGEDOUT,
        LOGGEDIN,
        GAMEPLAY
    }

    private UserInterface ui = new UserInterface(this);

    public UserInterface UI(){
        return this.ui;
    }

    private ServerFacade server = null;

    public ServerFacade Server(){
        return this.server;
    }

    private CommandProcessor commandProcessor = new CommandProcessor(this);

    public CommandProcessor Commands(){
        return commandProcessor;
    }

    private final HashMap<Integer, GameData> savedGameMap = new HashMap<>();

    public HashMap<Integer, GameData> SavedGames(){
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
