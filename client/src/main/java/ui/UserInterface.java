package ui;

import java.util.*;
import java.util.function.Consumer;

import static ui.EscapeSequences.*;

public class UserInterface {

    private Client client = null;
    private UIUtils uiUtils = new UIUtils();

    private boolean isRunning = true;

    public UserInterface(Client client){
        this.client = client;
    }

    public void startUI(){
        isRunning = true;

        while (isRunning){
            String[] command = uiUtils.promptCommand(client.isLoggedIn());
            client.Commands().processCommand(command);
        }
    }

    public void stopREPL(){
        isRunning = false;
    }



}
