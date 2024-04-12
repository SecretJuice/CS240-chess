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
    
    private UIUtils ui = new UIUtils();

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

    private Command[] inGameCommands = {
            new Command("help", this::helpCommand, "Provides help for using available commands"),
            new Command("leave", this::leaveCommand, "Exit the current game"),
            new Command("move", this::moveCommand, "Move a chess piece"),
            new Command("resign", this::resignCommand, "Forfeit the match"),
            new Command("redraw", this::redrawCommand, "Print out the chess board again (in case something messes it up)"),
            new Command("showmoves", this::showMovesCommand, "Show a pieces' valid moves"),

    };

    public CommandProcessor (Client client){
        this.client = client;
    }

    private Command[] getCommandList(Client.State state){
        return switch(state){
            case LOGGEDIN -> loggedInCommands;
            case LOGGEDOUT -> loggedOutCommands;
            case INGAME -> inGameCommands;
        };
    }

    public void processCommand(String[] args){
        if(args.length == 1){

            Command[] commands = getCommandList(client.getState());
            Command selectedCommand = null;

            String commandName = args[0];

            for(Command command : commands){
                if (Objects.equals(command.name(), commandName)){
                    selectedCommand = command;
                    break;
                }
            }

            if(selectedCommand == null){
                ui.printError("Unknown command ["+ commandName +"]\n");
            }
            else if(selectedCommand.function() == null){
                throw new RuntimeException("Command not implemented: " + selectedCommand.name() + "\n");
            }
            else {
                selectedCommand.function().run();
            }

        }
        else {

            ui.printError("Too many arguments\n");
        }
    }

    private void helpCommand(){

        Command[] commands = getCommandList(client.getState());

        for(Command command : commands){
            ui.printNormal("  " + command.name() + "  |  " + command.description() + "\n");
        }

    }

    private void quitCommand(){

        ui.printNormal("Quiting... thanks for playing!\n");

        client.ui().stopREPL();

    }

    private void registerCommand(){

        HashMap<String, String> registerParams = new HashMap<>();
        registerParams.put("username", "Username");
        registerParams.put("password", "Password");
        registerParams.put("email", "Email (Optional)");

        Map<String, String> userInputs = ui.promptParameters(registerParams);

        try{
            String username = client.server().register(userInputs.get("username"), userInputs.get("password"), userInputs.get("email"));
            ui.printNormal("Welcome, " + username + "!\n");
            client.setState(Client.State.LOGGEDIN);
        }
        catch(Exception e){
            ui.printError("Registration Failed: " + e.getMessage() + "\n");
        }
    }

    private void loginCommand(){

        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("username", "Username");
        loginParams.put("password", "Password");

        Map<String, String> userInputs = ui.promptParameters(loginParams);

        try{
            String username = client.server().login(userInputs.get("username"), userInputs.get("password"));
            ui.printNormal("Welcome, " + username + "!\n");
            client.setState(Client.State.LOGGEDIN);
        }
        catch(Exception e){
            ui.printError("Login Failed: " + e.getMessage() + "\n");
        }
    }

    private void logoutCommand(){

        try{
            client.server().logout();
            client.setState(Client.State.LOGGEDOUT);
        }
        catch (Exception e){
            ui.printError("Could not logout: " + e.getMessage() + "\n");
        }

    }

    private void listGamesCommand(){

        try{
            Collection<GameData> games = client.server().listGames();

            client.savedGames().clear();

            int index = 1;
            for(GameData game : games){

                String listing = "ID[ " + index + " ] " + game.gameName() + ": WHITE= " + game.whiteUsername() + ": BLACK= " + game.blackUsername();

                client.savedGames().put(index, game);

                ui.printNormal(listing + "\n");

                index += 1;

            }

            if (games.isEmpty()){
                ui.printNormal("There are currently no games. Use 'creategame' to start one!\n");
            }
        }
        catch(Exception e){
            ui.printError("Could not get games: " + "\n");
        }

    }

    private void joinGameCommand(){
        HashMap<String, String> joinGameParams = new HashMap<>();
        joinGameParams.put("gameID", "Game ID (Enter a number. i.e ID [ 1 ] would be 1)");
        joinGameParams.put("teamcolor", "Color (type WHITE or BLACK)");

        Map<String, String> userInputs = ui.promptParameters(joinGameParams);

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

            GameData desiredGame = client.savedGames().get(gameID);
            client.setJoinedGame(desiredGame);
            client.server().joinGame(desiredGame.gameID(), color);

            client.setState(Client.State.INGAME);
            ui.printNormal("Successfully joined the game! ID:["+ gameID +"]\n");


        }
        catch(Exception e){
            ui.printError("Could not join game: " + e.getMessage() + "\n");
            client.setJoinedGame(null);
        }
    }

    private void spectateCommand(){
        HashMap<String, String> spectateParams = new HashMap<>();
        spectateParams.put("gameID", "Game ID (Enter a number)");

        Map<String, String> userInputs = ui.promptParameters(spectateParams);

        Integer gameID = Integer.parseInt(userInputs.get("gameID"));

        try{
            GameData desiredGame = client.savedGames().get(gameID);
            client.setJoinedGame(desiredGame);
            client.server().joinGame(desiredGame.gameID(), null);

            ui.printNormal("Now spectating the game... ID[" + gameID + "]\n");
            client.setState(Client.State.INGAME);
        }
        catch(Exception e){
            ui.printError("Could not spectate game: " + e.getMessage() + "\n");
            client.setJoinedGame(null);
        }
    }

    private void createGameCommand(){
        HashMap<String, String> createGameParams = new HashMap<>();
        createGameParams.put("gameName", "Game Name");

        Map<String, String> userInputs = ui.promptParameters(createGameParams);

        String gameName = userInputs.get("gameName");

        try{
            client.server().createGame(gameName);

            ui.printNormal("Created Game!\n");
        }
        catch(Exception e){
            ui.printError("Could not create game: " + e.getMessage() + "\n");
        }
    }

    private void leaveCommand() {
        GameData currentGame = client.getJoinedGame();

        try{
            client.server().leaveGame(currentGame.gameID());
            ui.printNormal("Left the game" + "\n");
            client.setState(Client.State.LOGGEDIN);

        }
        catch(Exception e){
            ui.printError("Failed to leave: " + e.getMessage() + "\n");
        }
    }


    private void moveCommand(){
        GameData currentGame = client.getJoinedGame();

        HashMap<String, String> moveParams = new HashMap<>();
        moveParams.put("chessMove", "Move (e.g e2e4)");

        Map<String, String> userInputs = ui.promptParameters(moveParams);

        String moveText = userInputs.get("chessMove").toLowerCase().trim();

        try{
            ChessMove chessMove = createChessMove(moveText, currentGame.game().getBoard());
            client.server().makeMove(currentGame.gameID(), chessMove);
        }
        catch(Exception e){
            ui.printError("Could not move: " + e.getMessage());
        }
    }

    private ChessMove createChessMove(String moveText, ChessBoard board) throws Exception{
        if( moveText.length() != 4){
            throw new Exception("Must be exactly 4 characters long");
        }

        ChessPosition startPos = new ChessPosition((int)moveText.charAt(1) - 48, (int)moveText.charAt(0) - 96);
        ChessPosition endPos = new ChessPosition((int)moveText.charAt(3) - 48, (int)moveText.charAt(2) - 96);

        ChessPiece.PieceType promotionPiece = null;

        if (board.getPiece(startPos).getPieceType() == ChessPiece.PieceType.PAWN &&
                (endPos.getRow() == 8 || endPos.getRow() == 1)){

            HashMap<String, String> promotionParams = new HashMap<>();
            promotionParams.put("promotion", "Pawn Promotion: (e.g QUEEN, KNIGHT)");

            Map<String, String> userInputs = ui.promptParameters(promotionParams);

            try{
                promotionPiece = ChessPiece.PieceType.valueOf(userInputs.get("promotion").toUpperCase().trim());
            }
            catch(IllegalArgumentException e){
                ui.printError("Invalid piece type, selecting QUEEN by default");
                promotionPiece = ChessPiece.PieceType.QUEEN;
            }
        }

        return new ChessMove(startPos, endPos, promotionPiece);
    }

    private void resignCommand(){
        GameData currentGame = client.getJoinedGame();

        try{
            client.server().resignGame(currentGame.gameID());
            ui.printNormal("You resigned from the game" + "\n");

        }
        catch(Exception e){
            ui.printError("Failed to resign: " + e.getMessage() + "\n");
        }
    }
    private void showMovesCommand(){
        ui.printError("Whoops! Didn't make that :/" + "\n");
    }
    private void redrawCommand(){
        ChessGame.TeamColor playerColor = client.getJoinedGame().getPlayerTeam(client.server().getSession().username());

        BoardPainter painter = new BoardPainter();

        painter.paintBoard(client.getJoinedGame().game().getBoard(), playerColor);
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

            ui.printNormal("\n");

            painter.paintBoard(board, ChessGame.TeamColor.WHITE);
        }
        catch(Exception e){
            ui.printError(e.getMessage() + "\n");
        }
    }

}
