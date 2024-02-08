package server;

import server.handlers.Handler;
import server.handlers.RegisterHandler;
import spark.*;

import java.nio.file.Paths;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        registerEndpoints();

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * Initializes all of the servers endpoints
     */
    private void registerEndpoints(){
        Spark.delete("/db", (req, res) -> new Handler().handleRequest()); //Clear Application
        Spark.post("/user", (req, res) -> new RegisterHandler().handleRequest()); //Register User
        Spark.post("/session", (req, res) -> new Handler().handleRequest()); //Login
        Spark.delete("/session", (req, res) -> new Handler().handleRequest()); //Logout
        Spark.get("/game", (req, res) -> new Handler().handleRequest()); //List Games
        Spark.post("/game", (req, res) -> new Handler().handleRequest()); //Create Game
        Spark.put("/game", (req, res) -> new Handler().handleRequest()); //Join Game

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
