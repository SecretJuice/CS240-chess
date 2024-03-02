package web;

import com.google.gson.Gson;
import data.requests.BadRequestException;
import data.requests.ForbiddenException;
import data.requests.LoginRequest;
import data.requests.UnauthorizedException;
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


    public AuthData login(String username, String password) throws Exception{

        LoginRequest loginRequest = new LoginRequest(username, password);
        String body = parser.toJson(loginRequest);

        HTTPResponse response = connector.request(WebConnector.Method.POST, WebConnector.EndPoint.SESSION, null, body);

        SessionResponse sessionResponse = parser.fromJson(response.body(), SessionResponse.class);

        AuthData newSession = new AuthData(sessionResponse.authToken(), sessionResponse.username());
        setSession(newSession);

        return newSession;
    }



    private String extractError(String body){

        ErrorResponse errorData = parser.fromJson(body, ErrorResponse.class);
        return errorData.message();
    }

}
