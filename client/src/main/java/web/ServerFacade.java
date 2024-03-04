package web;

import com.google.gson.Gson;
import data.requests.*;
import data.responses.ErrorResponse;
import data.responses.HTTPResponse;
import data.responses.SessionResponse;
import model.AuthData;

public class ServerFacade {

    private final Gson parser = new Gson();
    private final WebConnector connector = new WebConnector("http://localhost:7777");
    private AuthData session;

    public AuthData getSession() {
        return session;
    }

    public void setSession(AuthData session) {
        this.session = session;
    }


    public String login(String username, String password) throws Exception{

        LoginRequest loginRequest = new LoginRequest(username, password);
        String body = parser.toJson(loginRequest);

        HTTPResponse response = connector.request(WebConnector.Method.POST, WebConnector.EndPoint.SESSION, null, body);

        SessionResponse sessionResponse = parser.fromJson(response.body(), SessionResponse.class);

        AuthData newSession = new AuthData(sessionResponse.authToken(), sessionResponse.username());
        setSession(newSession);

        return newSession.username();
    }

    public String register(String username, String password, String email) throws Exception{

        RegisterUserRequest registrationRequest = new RegisterUserRequest(username, password, email);
        String body = parser.toJson(registrationRequest);

        HTTPResponse response = connector.request(WebConnector.Method.POST, WebConnector.EndPoint.USER, null, body);

        SessionResponse sessionResponse = parser.fromJson(response.body(), SessionResponse.class);

        AuthData newSession = new AuthData(sessionResponse.authToken(), sessionResponse.username());
        setSession(newSession);

        return newSession.username();
    }

    public void logout() throws Exception{

        if(session == null){
            throw new UnauthorizedException("Not logged in.");
        }

        HTTPResponse response = connector.request(WebConnector.Method.DELETE, WebConnector.EndPoint.SESSION, session.authToken(), null);

    }


}
