package ui;

import chess.*;
import data.requests.BadRequestException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandProcessor {

    private Client client = null;

    private Command[] loggedOutCommands = {
            new Command("help", this::helpCommand, "Provides help for using available commands"),
            new Command("quit", this::quitCommand, "Exits the program"),
            new Command("login", this::loginCommand, "Login existing account"),
            new Command("register", this::registerCommand, "Create new account"),
            new Command("paintboard", this::paintboardCommand, "[TEST] displays default chess board."),
    };

    private Command[] loggedInCommands = {
            new Command("help", this::helpCommand, "Provides help for using available commands"),
            new Command("quit", this::quitCommand, "Exits the program"),
            new Command("logout", this::logoutCommand, "End current session"),
            new Command("listgames", this::listGamesCommand, "Browse all games to join"),
            new Command("creategame", this::createGameCommand, "Create a new game"),
            new Command("join", this::joinGameCommand, "Join listed games"),
            new Command("spectate", this::spectateCommand, "Spectate a game in progress"),
    };

    public CommandProcessor (Client client){
        this.client = client;
    }

    public void processCommand(String[] args){
        if(args.length == 1){

            Command[] commands = client.isLoggedIn() ? loggedInCommands : loggedOutCommands;
            Command selectedCommand = null;

            String commandName = args[0];

            for(Command command : commands){
                if (Objects.equals(command.name(), commandName)){
                    selectedCommand = command;
                    break;
                }
            }

            if(selectedCommand == null){
                client.UI().printError("Unknown command ["+ commandName +"]\n");
            }
            else if(selectedCommand.function() == null){
                throw new RuntimeException("Command not implemented: " + selectedCommand.name() + "\n");
            }
            else {
                selectedCommand.function().run();
            }

        }
        else {

            client.UI().printError("Too many arguments\n");
        }
    }

    private void helpCommand(){

        Command[] commands = client.isLoggedIn() ? loggedInCommands : loggedOutCommands;

        for(Command command : commands){
            client.UI().printNormal("  " + command.name() + "  |  " + command.description() + "\n");
        }

    }

    private void quitCommand(){

        client.UI().printNormal("Quiting... thanks for playing!\n");

        client.UI().stopREPL();

    }

    private void registerCommand(){

        HashMap<String, String> registerParams = new HashMap<>();
        registerParams.put("username", "Username");
        registerParams.put("password", "Password");
        registerParams.put("email", "Email (Optional)");

        Map<String, String> userInputs = client.UI().promptParameters(registerParams);

        try{
            String username = client.Server().register(userInputs.get("username"), userInputs.get("password"), userInputs.get("email"));
            client.UI().printNormal("Welcome, " + username + "!\n");
        }
        catch(Exception e){
            client.UI().printError("Registration Failed: " + e.getMessage() + "\n");
        }
    }

    private void loginCommand(){

        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("username", "Username");
        loginParams.put("password", "Password");

        Map<String, String> userInputs = client.UI().promptParameters(loginParams);

        try{
            String username = client.Server().login(userInputs.get("username"), userInputs.get("password"));
            client.UI().printNormal("Welcome, " + username + "!\n");
        }
        catch(Exception e){
            client.UI().printError("Login Failed: " + e.getMessage() + "\n");
        }
    }

    private void logoutCommand(){

        try{
            client.Server().logout();
        }
        catch (Exception e){
            client.UI().printError("Could not logout: " + e.getMessage() + "\n");
        }

    }

    private void listGamesCommand(){

        try{
            Collection<GameData> games = client.Server().listGames();

            client.SavedGames().clear();

            int index = 1;
            for(GameData game : games){

                String listing = "ID[ " + index + " ] " + game.gameName() + ": WHITE= " + game.whiteUsername() + ": BLACK= " + game.blackUsername();

                client.SavedGames().put(index, game);

                client.UI().printNormal(listing + "\n");

                index += 1;

            }

            if (games.isEmpty()){
                client.UI().printNormal("There are currently no games. Use 'creategame' to start one!\n");
            }
        }
        catch(Exception e){
            client.UI().printError("Could not get games: " + "\n");
        }

    }

    private void joinGameCommand(){
        HashMap<String, String> joinGameParams = new HashMap<>();
        joinGameParams.put("gameID", "Game ID (Enter a number. i.e ID [ 1 ] would be 1)");
        joinGameParams.put("teamcolor", "Color (type WHITE or BLACK)");

        Map<String, String> userInputs = client.UI().promptParameters(joinGameParams);

        BoardPainter painter = new BoardPainter();

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

            GameData desiredGame = client.SavedGames().get(gameID);

            client.Server().joinGame(desiredGame.gameID(), color);

            client.UI().printNormal("Successfully joined the game! ID:["+ gameID +"]\n");

            paintBoards();

        }
        catch(Exception e){
            client.UI().printError("Could not join game: " + e.getMessage() + "\n");
        }
    }

    private void spectateCommand(){
        HashMap<String, String> spectateParams = new HashMap<>();
        spectateParams.put("gameID", "Game ID (Enter a number)");

        Map<String, String> userInputs = client.UI().promptParameters(spectateParams);

        Integer gameID = Integer.parseInt(userInputs.get("gameID"));

        try{
            GameData desiredGame = client.SavedGames().get(gameID);

            client.Server().joinGame(desiredGame.gameID(), null);

            client.UI().printNormal("Now spectating the game... ID[" + gameID + "]\n");

            paintBoards();
        }
        catch(Exception e){
            client.UI().printError("Could not spectate game: " + e.getMessage() + "\n");
        }
    }

    private void createGameCommand(){
        HashMap<String, String> createGameParams = new HashMap<>();
        createGameParams.put("gameName", "Game Name");

        Map<String, String> userInputs = client.UI().promptParameters(createGameParams);

        String gameName = userInputs.get("gameName");

        try{
            client.Server().createGame(gameName);

            client.UI().printNormal("Created Game!\n");
        }
        catch(Exception e){
            client.UI().printError("Could not create game: " + e.getMessage() + "\n");
        }
    }

    private void paintboardCommand() {

        paintBoards();

    }

    private void paintBoards(){
        BoardPainter painter = new BoardPainter();

        try{
            ChessGame game = new ChessGame();
            game.getBoard().resetBoard();

            ChessBoard board = game.getBoard();

            painter.paintBoard(board, ChessGame.TeamColor.BLACK);

            client.UI().printNormal("\n");

            painter.paintBoard(board, ChessGame.TeamColor.WHITE);
        }
        catch(Exception e){
            client.UI().printError(e.getMessage() + "\n");
        }
    }

}
