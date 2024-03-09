package ui;

import web.ServerFacade;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class UserInterface {

    private ServerFacade serverFacade = null;

    private Command[] loggedOutCommands = {
            new Command("help", this::helpCommand, "Provides help for using available commands"),
            new Command("quit", null, "Exits the program"),
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
        boolean running = true;

        while (running){
            String[] command = promptCommand(isLoggedIn());
            processCommand(command);
        }
    }

    private String[] promptCommand(boolean loggedIn){
        String prefix = loggedIn ? "[LOGGED IN]>>> " : "[LOGGED OUT]>>> ";
        System.out.print(prefix);

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
                System.err.println("Unknown command ["+ commandName +"]");
            }
            else if(selectedCommand.function() == null){
                throw new RuntimeException("Command not implemented: " + selectedCommand.name());
            }
            else {
                selectedCommand.function().run();
            }

        }
        else {

            System.err.println("Too many arguments");
        }
    }

    private void helpCommand(){

        Command[] commands = isLoggedIn() ? loggedInCommands : loggedOutCommands;

        for(Command command : commands){
            System.out.println("  " + command.name() + "  |  " + command.description());
        }

    }

    private boolean isLoggedIn(){

        if (serverFacade != null){

            return serverFacade.getSession() != null;
        }

        return false;

    }



}
