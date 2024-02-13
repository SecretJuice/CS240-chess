package server.requests;

public record RegisterUserRequest(String username, String password, String email) { }
