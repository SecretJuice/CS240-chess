package ui;

import chess.ChessGame;
import data.requests.BadRequestException;
import model.GameData;
import web.ServerFacade;

import java.util.*;
import java.util.function.Consumer;

import static ui.EscapeSequences.*;

public class UserInterface {

    private ServerFacade serverFacade = null;

    private boolean isRunning = true;

    private Command[] loggedOutCommands = {
            new Command("help", this::helpCommand, "Provides help for using available commands"),
            new Command("quit", this::quitCommand, "Exits the program"),
            new Command("login", this::loginCommand, "Login existing account"),
            new Command("register", this::registerCommand, "Create new account"),
    };

    private Command[] loggedInCommands = {
            new Command("help", this::helpCommand, "Provides help for using available commands"),
            new Command("quit", this::quitCommand, "Exits the program"),
            new Command("logout", this::logoutCommand, "End current session"),
            new Command("listgames", this::listGamesCommand, "Browse all games to join"),
            new Command("creategame", null, "Create a new game"),
            new Command("join", this::joinGameCommand, "Join listed games"),
            new Command("spectate", null, "Spectate a game in progress"),
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

        return prompt(prefix, this::printNormal).split(" ");
    }

    private String prompt(String prefix, Consumer<String> printFunction){
        printFunction.accept(prefix);

        Scanner scanner = new Scanner(System.in);

        return scanner.next();
    }

    /*
    Takes in Map of parameter names and display values.
    Returns a Map of parameter names and user input
     */
    private Map<String, String> promptParameters(HashMap<String, String> params){

        HashMap<String, String> parameterPairs = new HashMap<>();

        for (Map.Entry<String, String> entry : params.entrySet()){
            parameterPairs.put(entry.getKey(), prompt(entry.getValue() + " : ", this::printNormal));
        }

        return parameterPairs;

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

        printNormal("Quiting... thanks for playing!\n");

        isRunning = false;

    }

    private void registerCommand(){

        HashMap<String, String> registerParams = new HashMap<>();
        registerParams.put("username", "Username");
        registerParams.put("password", "Password");
        registerParams.put("email", "Email (Optional)");

        Map<String, String> userInputs = promptParameters(registerParams);

        try{
            String username = serverFacade.register(userInputs.get("username"), userInputs.get("password"), userInputs.get("email"));
            printNormal("Welcome, " + username + "!\n");
        }
        catch(Exception e){
            printError("Registration Failed: " + e.getMessage() + "\n");
        }
    }

    private void loginCommand(){

        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("username", "Username");
        loginParams.put("password", "Password");

        Map<String, String> userInputs = promptParameters(loginParams);

        try{
            String username = serverFacade.login(userInputs.get("username"), userInputs.get("password"));
            printNormal("Welcome, " + username + "!\n");
        }
        catch(Exception e){
            printError("Login Failed: " + e.getMessage() + "\n");
        }
    }

    private void logoutCommand(){

        try{
            serverFacade.logout();
        }
        catch (Exception e){
            printError("Could not logout: " + e.getMessage() + "\n");
        }

    }

    private void listGamesCommand(){

        try{
            Collection<GameData> games = serverFacade.listGames();
            for(GameData game : games){

                printNormal(game.toString() + "\n");

            }

            if (games.isEmpty()){
                printNormal("There are currently no games. Use 'creategame' to start one!\n");
            }
        }
        catch(Exception e){
            printError("Could not get games: " + "\n");
        }

    }

    private void joinGameCommand(){
        HashMap<String, String> joinGameParams = new HashMap<>();
        joinGameParams.put("gameID", "Game ID (Enter a number)");
        joinGameParams.put("teamcolor", "Color (type WHITE or BLACK)");

        Map<String, String> userInputs = promptParameters(joinGameParams);

        try{

            Integer gameID = Integer.parseInt(userInputs.get("gameID"));
            ChessGame.TeamColor color = null;

            if(userInputs.get("teamcolor").toUpperCase().equals("WHITE")){
                color = ChessGame.TeamColor.WHITE;
            }
            else if (userInputs.get("teamcolor").toUpperCase().equals("BLACK")){
                color = ChessGame.TeamColor.BLACK;
            }
            else{
                throw new BadRequestException("Please enter a valid color (WHITE or BLACK)");
            }

            serverFacade.joinGame(gameID, color);

            printNormal("Successfully joined the game! ID:["+ gameID +"]\n");
        }
        catch(Exception e){
            printError("Could not join game: " + e.getMessage() + "\n");
        }
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
