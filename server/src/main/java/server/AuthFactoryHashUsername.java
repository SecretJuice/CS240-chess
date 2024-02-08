package server;

import model.AuthData;

public class AuthFactoryHashUsername implements AuthFactory{
    public AuthData createAuthData(String username) {
        return new AuthData(Integer.toString(username.hashCode()), username);
    }
}
