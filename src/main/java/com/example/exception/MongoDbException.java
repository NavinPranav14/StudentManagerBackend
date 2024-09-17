package com.example.exception;

public class MongoDbException extends Exception {

    public MongoDbException(String message, Exception e) {
        super(message, e);
    }

    public MongoDbException(String message) {
        super(message);
    }

    public MongoDbException(Exception e) {
        super(e);
    }
}

