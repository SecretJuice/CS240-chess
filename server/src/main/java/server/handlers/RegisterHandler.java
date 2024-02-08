package server.handlers;

import dataAccess.LocalAuthDAO;
import dataAccess.LocalUserDAO;
import model.AuthData;
import model.UserData;
import server.services.UserRegistrationService;

public class RegisterHandler extends Handler{
    @Override
    public String handleRequest(){

        UserData userData = new UserData("Yourmom", "password1234", "yourmom@mom.com");

        AuthData authData = new UserRegistrationService().registerUser(userData, new LocalUserDAO(), new LocalAuthDAO());

        return String.format("{\"authToken\":\"{%s}\"}", authData.authToken());
    }
}
