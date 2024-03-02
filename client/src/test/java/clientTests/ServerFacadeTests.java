package clientTests;

import data.requests.UnauthorizedException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import web.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(7777);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testLoginNonexistant() {

        ServerFacade serverFacade = new ServerFacade();
        try{
            serverFacade.login("NonexistentUser", "BadPassword");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        assertThrows(UnauthorizedException.class, () -> serverFacade.login("NonexistentUser", "BadPassword"));

    }

}
