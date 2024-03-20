package ui;

public record Command(String name, Runnable function, String description) {  }
