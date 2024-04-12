package ui;

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
            String prefix = client.isLoggedIn() ? "[LOGGED IN]>>" : "[LOGGED OUT]>>";
            String[] command = uiUtils.promptCommand(prefix);
            client.commands().processCommand(command);
        }
    }

    public void stopREPL(){
        isRunning = false;
    }



}
