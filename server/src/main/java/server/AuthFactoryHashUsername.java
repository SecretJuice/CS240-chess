package server;

import model.AuthData;

public class AuthFactoryHashUsername implements DataFactory<AuthData>{
    public AuthData createData(String username) {
        return new AuthData(Integer.toString(username.hashCode()), username);
    }
}
