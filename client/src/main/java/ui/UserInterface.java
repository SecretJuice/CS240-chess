package ui;

import java.util.*;
import java.util.function.Consumer;

import static ui.EscapeSequences.*;

public class UserInterface {

    private Client client = null;

    private boolean isRunning = true;

    public UserInterface(Client client){
        this.client = client;
    }

    public void startUI(){
        isRunning = true;

        while (isRunning){
            String[] command = promptCommand(client.isLoggedIn());
            client.Commands().processCommand(command);
        }
    }

    public void stopREPL(){
        isRunning = false;
    }

    private String[] promptCommand(boolean loggedIn){
        String prefix = loggedIn ? "[LOGGED IN]>>> " : "[LOGGED OUT]>>> ";

        return prompt(prefix, this::printNormal).split(" ");
    }

    private String prompt(String prefix, Consumer<String> printFunction){
        printFunction.accept(prefix);

        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    /*
    Takes in Map of parameter names and display values.
    Returns a Map of parameter names and user input
     */
    public Map<String, String> promptParameters(HashMap<String, String> params){

        HashMap<String, String> parameterPairs = new HashMap<>();

        for (Map.Entry<String, String> entry : params.entrySet()){
            parameterPairs.put(entry.getKey(), prompt(entry.getValue() + " : ", this::printNormal));
        }

        return parameterPairs;

    }




    /*
    Takes in base text and augments it with a given set of PREFIX escape sequences,
    then sends it to System.out.print() *MAKE SURE TO INCLUDE \n*
    Will reset everything at the end of the string.
     */
    private void printDecoratedString(String text, String decorators, String resets){
        String output = decorators + text + resets;
        System.out.print(output);
    }

    public void printNormal(String text){
        printDecoratedString(text, SET_TEXT_COLOR_WHITE, RESET_TEXT_COLOR);
    }

    public void printError(String text){
        printDecoratedString(text, SET_TEXT_COLOR_RED+SET_TEXT_ITALIC, RESET_TEXT_COLOR+RESET_TEXT_ITALIC);
    }

}
