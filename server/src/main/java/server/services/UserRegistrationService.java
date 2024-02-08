package server.services;

import dataAccess.AuthDataAccess;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserRegistrationService {

    public AuthData registerUser(UserData userData, UserDataAccess userDAO, AuthDataAccess authDAO){

        try {
            UserData existingUser = userDAO.getUser(userData.username());
            if (existingUser == null){
                userDAO.createUser(userData);
            }

            AuthData authData = new AuthData(UUID.randomUUID().toString(), userData.username());

            authDAO.createAuth(authData);

            return authData;

        } catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

}
