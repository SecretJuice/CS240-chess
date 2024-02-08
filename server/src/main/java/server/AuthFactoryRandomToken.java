package server;

import model.AuthData;

import java.util.UUID;

public class AuthFactoryRandomToken implements AuthFactory{
    public AuthData createAuthData(String username) {
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
