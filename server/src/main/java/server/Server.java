package server;

import server.handlers.*;
import server.requests.BadRequestException;
import server.requests.ForbiddenException;
import server.requests.UnauthorizedException;
import spark.*;

import java.nio.file.Paths;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        registerEndpoints();
        mapExceptions();

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * Initializes all of the servers endpoints
     */
    private void registerEndpoints(){
        Spark.delete("/db", (req, res) -> new ClearAppHandler().handle(req, res)); //Clear Application
        Spark.post("/user", (req, res) -> new RegisterHandler().handle(req, res)); //Register User
        Spark.post("/session", (req, res) -> new LoginHandler().handle(req, res)); //Login
        Spark.delete("/session", (req, res) -> new LogoutHandler().handle(req, res)); //Logout
        Spark.get("/game", (req, res) -> new ListGameHandler().handle(req, res)); //List Games
        Spark.post("/game", (req, res) -> new CreateGameHandler().handle(req, res)); //Create Game
        Spark.put("/game", (req, res) -> new JoinGameHandler().handle(req, res)); //Join Game

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
}
