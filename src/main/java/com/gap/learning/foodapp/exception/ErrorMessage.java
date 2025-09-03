package com.gap.learning.foodapp.exception;

public class ErrorMessage {

    private int statusCode;
    private String message;
    private String description;

    public ErrorMessage(int statusCode, String message, String description) {
        this.statusCode = statusCode;
        this.message = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ErrorMessage setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ErrorMessage setDescription(String description) {
        this.description = description;
        return this;
    }
}
