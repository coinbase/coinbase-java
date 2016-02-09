package com.coinbase.v2.models.errors;

public class ErrorBody {
    private String id;
    private String message;

    public ErrorBody() {
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
