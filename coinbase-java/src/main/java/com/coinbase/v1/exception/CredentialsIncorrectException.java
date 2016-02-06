package com.coinbase.v1.exception;

public class CredentialsIncorrectException extends CoinbaseException {
    
    public CredentialsIncorrectException() {
        super("The provided credentials are invalid");
    }
    
}
