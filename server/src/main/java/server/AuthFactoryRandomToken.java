package server;

import dataAccess.DataAccessObject;
import model.AuthData;

import java.util.UUID;

public class AuthFactoryRandomToken implements DataFactory<AuthData> {
    public AuthData createData(String username) {
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
