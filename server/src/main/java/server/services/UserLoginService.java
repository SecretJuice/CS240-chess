package server.services;

import dataAccess.DataAccessObject;
import model.AuthData;
import model.UserData;
import server.AuthFactory;

import java.util.Objects;

public class UserLoginService extends Service{

    public AuthData loginUser(UserData userData, DataAccessObject<UserData> userDAO, DataAccessObject<AuthData> authDAO, AuthFactory authFactory) throws ServiceException{

        try{

            UserData existingUser = userDAO.get(userData.username());

            if (existingUser == null){
                throw new ServiceException("User does not exist");
            }

            if (!Objects.equals(existingUser.password(), userData.password())){
                throw new ServiceException("Password is incorrect");
            }

            return createSession(userData, authDAO, authFactory);

        }
        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }



}
