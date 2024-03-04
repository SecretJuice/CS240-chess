package server;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.handlers.*;
import data.requests.BadRequestException;
import data.requests.ForbiddenException;
import data.requests.UnauthorizedException;
import spark.*;

public class Server {

    private DataAccessObject<UserData> userDAO;
    private DataAccessObject<AuthData> authDAO;
    private DataAccessObject<GameData> gameDAO;

    public int run(int desiredPort) {
        return run(desiredPort, false);
    }

    public int run(int desiredPort, boolean localDataStore){
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        initializeDataAccess(localDataStore);
        registerEndpoints();
        mapExceptions();

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * Initializes all the servers endpoints
     */
    private void registerEndpoints(){
        Spark.delete("/db", (req, res) -> new ClearAppHandler(userDAO, authDAO, gameDAO).handle(req, res)); //Clear Application
        Spark.post("/user", (req, res) -> new RegisterHandler(userDAO, authDAO).handle(req, res)); //Register User
        Spark.post("/session", (req, res) -> new LoginHandler(userDAO, authDAO).handle(req, res)); //Login
        Spark.delete("/session", (req, res) -> new LogoutHandler(authDAO).handle(req, res)); //Logout
        Spark.get("/game", (req, res) -> new ListGameHandler(authDAO, gameDAO).handle(req, res)); //List Games
        Spark.post("/game", (req, res) -> new CreateGameHandler(authDAO, gameDAO).handle(req, res)); //Create Game
        Spark.put("/game", (req, res) -> new JoinGameHandler(authDAO, gameDAO).handle(req, res)); //Join Game

    }

    /**
     * Initializes exception handling
     */
    private void mapExceptions(){

        Spark.exception(UnauthorizedException.class, (e, req, res) -> {
            res.status(401);
            res.body("{\"message\": \"Error: Unauthorized -> " + e.getMessage() +"\"}");
        });

        Spark.exception(BadRequestException.class, (e, req, res) -> {
            res.status(400);
            res.body("{\"message\": \"Error: Bad Request -> " + e.getMessage() +"\"}");
        });

        Spark.exception(ForbiddenException.class, (e, req, res) -> {
            res.status(403);
            res.body("{\"message\": \"Error: Forbidden -> " + e.getMessage() + "\"}");
        });

        Spark.exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.body("{\"message\": \"Error: Internal Error -> " + e.getMessage() + "\"}");
        });
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    void initializeDataAccess(boolean local){

        try{
            if (local){
                userDAO = new LocalUserDAO();
                authDAO = new LocalAuthDAO();
                gameDAO = new LocalGameDAO();
            }
            else{
                userDAO = new SQLUserDAO();
                authDAO = new SQLAuthDAO();
                gameDAO = new SQLGameDAO();
            }
        }
        catch(DataAccessException e){
            System.err.println("Could not initialize SQL Data Access: " + e.getMessage());
            userDAO = new LocalUserDAO();
            authDAO = new LocalAuthDAO();
            gameDAO = new LocalGameDAO();
        }
    }
}
