package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.DataFactory;
import data.requests.UnauthorizedException;

import java.util.Objects;

public class UserLoginService extends Service{

    private final DataAccessObject<UserData> userDAO;
    private final DataAccessObject<AuthData> authDAO;
    private final DataFactory<AuthData> authFactory;

    public UserLoginService(DataAccessObject<UserData> userDataAccess, DataAccessObject<AuthData> authDataAccess, DataFactory<AuthData> authDataFactory){

        userDAO = userDataAccess;
        authDAO = authDataAccess;
        authFactory = authDataFactory;

    }


    public AuthData loginUser(UserData userData) throws Exception{


        UserData existingUser = userDAO.get(userData.username());

        if (existingUser == null){
            throw new UnauthorizedException("User does not exist");
        }

        if (!Objects.equals(existingUser.password(), userData.password())){
            throw new UnauthorizedException("Password is incorrect");
        }

        return createSession(userData, authDAO, authFactory);

    }



}
