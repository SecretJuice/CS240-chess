package ui;

import web.ServerFacade;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UserInterface {

    private ServerFacade serverFacade = null;

    private boolean isRunning = true;

    private Command[] loggedOutCommands = {
            new Command("help", this::helpCommand, "Provides help for using available commands"),
            new Command("quit", this::quitCommand, "Exits the program"),
            new Command("login", null, "Login existing account"),
            new Command("register", null, "Create new account"),
    };

    private Command[] loggedInCommands = {
            new Command("help", null, "Provides help for using available commands"),
            new Command("quit", null, "Exits the program"),
            new Command("login", null, "Login existing account"),
            new Command("register", null, "Create new account"),
    };

    public UserInterface(ServerFacade serverFacade){
        this.serverFacade = serverFacade;
    }

    public void startUI(){
        isRunning = true;

        while (isRunning){
            String[] command = promptCommand(isLoggedIn());
            processCommand(command);
        }
    }

    private String[] promptCommand(boolean loggedIn){
        String prefix = loggedIn ? "[LOGGED IN]>>> " : "[LOGGED OUT]>>> ";
        printNormal(prefix);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        return input.split(" ");
    }

    private void processCommand(String[] args){
        if(args.length == 1){

            Command[] commands = isLoggedIn() ? loggedInCommands : loggedOutCommands;
            Command selectedCommand = null;

            String commandName = args[0];

            for(Command command : commands){
                if (Objects.equals(command.name(), commandName)){
                    selectedCommand = command;
                    break;
                }
            }

            if(selectedCommand == null){
                printError("Unknown command ["+ commandName +"]\n");
            }
            else if(selectedCommand.function() == null){
                throw new RuntimeException("Command not implemented: " + selectedCommand.name() + "\n");
            }
            else {
                selectedCommand.function().run();
            }

        }
        else {

            printError("Too many arguments\n");
        }
    }

    private void helpCommand(){

        Command[] commands = isLoggedIn() ? loggedInCommands : loggedOutCommands;

        for(Command command : commands){
            printNormal("  " + command.name() + "  |  " + command.description() + "\n");
        }

    }

    private void quitCommand(){

        Command[] commands = isLoggedIn() ? loggedInCommands : loggedOutCommands;

        printNormal("Quiting... thanks for playing!\n");

        isRunning = false;

    }

    private boolean isLoggedIn(){

        if (serverFacade != null){

            return serverFacade.getSession() != null;
        }
        return false;
    }

    /*
    Takes in base text and augments it with a given set of PREFIX escape sequences,
    then sends it to System.out.print() *MAKE SURE TO INCLUDE \n*
    Will reset everything at the end of the string.
     */
    private void printDecorateString(String text, String decorators, String resets){
        String output = decorators + text + resets;
        System.out.print(output);
    }

    private void printNormal(String text){
        printDecorateString(text, SET_TEXT_COLOR_WHITE, RESET_TEXT_COLOR);
    }

    private void printError(String text){
        printDecorateString(text, SET_TEXT_COLOR_RED, RESET_TEXT_COLOR);
    }

}
