package web;

import com.google.gson.Gson;
import data.requests.BadRequestException;
import data.requests.ForbiddenException;
import data.requests.UnauthorizedException;
import data.responses.ErrorResponse;
import data.responses.HTTPResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class WebConnector {

    private final String url;
    private final Gson parser = new Gson();

    public WebConnector(String baseURL){

        if (baseURL.charAt(baseURL.length() - 1) == '/'){
            url = baseURL;
        }
        else {
            url = baseURL + '/';
        }
    }

    public enum EndPoint {
        DB,
        USER,
        SESSION,
        GAME
    }

    public enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    public HTTPResponse request(Method method, EndPoint endPoint, String authToken, String body) throws Exception {



        HttpURLConnection http = sendRequest(method, endPoint, authToken, body);


        if(http.getResponseCode() >= 400){

            HTTPResponse response = new HTTPResponse(http.getResponseCode(), readErrorBody(http));

            throwExceptions(response);
        }

        String responseBody = readResponseBody(http);

        return new HTTPResponse(http.getResponseCode(), responseBody);


    }

    private HttpURLConnection sendRequest(Method method, EndPoint endPoint, String authToken, String body) throws URISyntaxException, IOException {
        URI uri = new URI(this.url + endPoint.toString().toLowerCase());
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setRequestMethod(method.toString());
        if (authToken != null){
            http.addRequestProperty("Authorization", authToken);
        }

        writeRequestBody(body, http);
        http.connect();
        return http;
    }

    private void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if (body != null && (!body.isEmpty())) {
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }


    private String readResponseBody(HttpURLConnection http) throws IOException {
        StringBuilder responseBody = new StringBuilder();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String line;
            while((line = br.readLine()) != null){

                responseBody.append(line);
            }
        }
        return responseBody.toString();
    }

    private String readErrorBody(HttpURLConnection http) throws IOException {
        StringBuilder responseBody = new StringBuilder();

        try (InputStream respBody = http.getErrorStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String line;
            while((line = br.readLine()) != null){

                responseBody.append(line);
            }
        }
        return responseBody.toString();
    }

    private void throwExceptions(HTTPResponse response) throws Exception{

        ErrorResponse errorBody = parser.fromJson(response.body(), ErrorResponse.class);
        String message = errorBody.message();

        switch(response.code()){
            case 400: throw new BadRequestException(message);
            case 401: throw new UnauthorizedException(message);
            case 403: throw new ForbiddenException(message);
            case 500: throw new Exception(message);
        }

    }
}
