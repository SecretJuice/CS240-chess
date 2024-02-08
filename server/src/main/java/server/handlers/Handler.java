package server.handlers;

import dataAccess.DataAccessException;

public class Handler {

    public String handleRequest() throws DataAccessException {
        System.out.println("Handling something ig");

        String yourmom = "{\"response\": \"yourmom\"}";

        return yourmom;
    }

}
