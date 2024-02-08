package server;

import model.AuthData;

public interface AuthFactory {
    public AuthData createAuthData(String username);
}
