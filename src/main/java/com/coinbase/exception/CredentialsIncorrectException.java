package com.coinbase.exception;

public class CredentialsIncorrectException extends CoinbaseException {
    
    public CredentialsIncorrectException() {
        super("The provided credentials are invalid");
    }
    
}
